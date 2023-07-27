data "aws_s3_object" "stream_api_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "api-0.1-all.jar"
}

data "aws_s3_object" "stream_video_upload_event_publisher_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "video-upload-event-publisher-0.1-all.jar"
}

data "aws_s3_object" "stream_video_enrich_metadata_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "enrich-video-details-with-metadata-0.1-all.jar"
}

resource "aws_lambda_function" "video-stream-api" {
  s3_bucket     = "ssp-sandbox-video-stream-apps-store"
  s3_key        = "api-0.1-all.jar"
  function_name = "video-stream-api${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.stream_api_package.version_id

  handler = "io.micronaut.function.aws.proxy.MicronautLambdaHandler"
  runtime = "java17"
  timeout = 30
  memory_size = 256

  role = aws_iam_role.lambda_api_exec.arn

  environment {
    variables = {
      DYNAMODB_VIDEO_DETAILS_TABLE_NAME = "video_details${local.environmentSuffix}",
      S3_VIDEOS_BUCKET = "ssp-sandbox-video-stream-video-upload-hold${local.environmentSuffix}"
    }
  }
}

resource "aws_lambda_function" "video-upload-event-publisher" {
  s3_bucket     = "ssp-sandbox-video-stream-apps-store"
  s3_key        = "video-upload-event-publisher-0.1-all.jar"
  function_name = "video-stream-upload-event-publisher${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.stream_video_upload_event_publisher_package.version_id

  handler = "ssp.video.stream.FunctionRequestHandler"
  runtime = "java17"
  timeout = 30

  role = aws_iam_role.upload_hold_iam_for_lambda.arn

  environment {
    variables = {
      EVENTBRIDGE_BUS_NAME = "events${local.environmentSuffix}"
    }
  }
}

resource "aws_lambda_function" "video-metadata-enricher" {
  s3_bucket     = "ssp-sandbox-video-stream-apps-store"
  s3_key        = "enrich-video-details-with-metadata-0.1-all.jar"
  function_name = "video-stream-enrich-video-with-metadata${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.stream_video_enrich_metadata_package.version_id

  handler = "ssp.video.stream.FunctionRequestHandler"
  runtime = "java17"
  timeout = 30

  role = aws_iam_role.enrich_with_metadata_lambda_role.arn

  environment {
    variables = {
      METADATA_ENRICH_TABLE_NAME = "video_details${local.environmentSuffix}"
    }
  }
}

resource "aws_iam_role" "lambda_api_exec" {
  name = "video_stream_api-role${local.environmentSuffix}"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF

}

resource "aws_iam_policy" "lambda_put_dynamodb_policy" {
  name   = "lambda-put-dynamodb-policy${local.environmentSuffix}"
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        Action : [
          "dynamodb:getItem",
          "dynamodb:PutItem"
        ],
        Effect : "Allow",
        Resource : "*"
      }
    ]
  })
}

resource "aws_iam_policy" "lambda_s3_signed_url_policy" {
  name   = "lambda-s3-signed-url-policy${local.environmentSuffix}"
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        Action : [
          "s3:*"
        ],
        Effect : "Allow",
        Resource : "*"
      }
    ]
  })
}

resource "aws_iam_policy" "lambda_put_events_policy" {
  name   = "lambda-put-events-policy${local.environmentSuffix}"
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        Action : [
          "events:PutEvents"
        ],
        Effect : "Allow",
        Resource : "*"
      }
    ]
  })
}

resource "aws_iam_policy" "function_logging_policy" {
  name   = "function-logging-policy${local.environmentSuffix}"
  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        Action : [
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ],
        Effect : "Allow",
        Resource : "arn:aws:logs:*:*:*"
      }
    ]
  })
}

data "aws_iam_policy_document" "upload_hold-iam-policy" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }

    actions = ["sts:AssumeRole"]
  }
}

resource "aws_iam_role" "upload_hold_iam_for_lambda" {
  name               = "upload_hold_iam_for_lambda${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.upload_hold-iam-policy.json
}

data "aws_iam_policy_document" "enrich_with_metadata_lambda_role_iam_policy" {
  statement {
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "enrich_with_metadata_lambda_role" {
  name               = "EnrichMetadataLambdaRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.enrich_with_metadata_lambda_role_iam_policy.json
}

resource "aws_lambda_permission" "apigw" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.video-stream-api.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.video-stream-api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "allow_bucket" {
  statement_id  = "AllowExecutionFromS3Bucket"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.video-upload-event-publisher.arn
  principal     = "s3.amazonaws.com"
  source_arn    = aws_s3_bucket.upload_hold.arn
}

resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = aws_s3_bucket.upload_hold.id

  lambda_function {
    lambda_function_arn = aws_lambda_function.video-upload-event-publisher.arn
    events              = ["s3:ObjectCreated:*"]
  }

  depends_on = [aws_lambda_permission.allow_bucket]
}

resource "aws_iam_role_policy_attachment" "api_put_dynamodb_policy_attachment" {
  role       = aws_iam_role.lambda_api_exec.id
  policy_arn = aws_iam_policy.lambda_put_dynamodb_policy.arn
}

resource "aws_iam_role_policy_attachment" "enrich_metadata_put_dynamodb_policy_attachment" {
  role       = aws_iam_role.enrich_with_metadata_lambda_role.id
  policy_arn = aws_iam_policy.lambda_put_dynamodb_policy.arn
}

resource "aws_iam_role_policy_attachment" "api_s3_signed_url_policy_attachment" {
  role       = aws_iam_role.lambda_api_exec.id
  policy_arn = aws_iam_policy.lambda_s3_signed_url_policy.arn
}

resource "aws_iam_role_policy_attachment" "api_logging_policy_attachment" {
  role       = aws_iam_role.lambda_api_exec.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "upload_hold_logging_policy_attachment" {
  role       = aws_iam_role.upload_hold_iam_for_lambda.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "upload_hold_put_events_policy_attachment" {
  role       = aws_iam_role.upload_hold_iam_for_lambda.id
  policy_arn = aws_iam_policy.lambda_put_events_policy.arn
}

resource "aws_iam_role_policy_attachment" "metadata_enrich_logging_policy_attachment" {
  role       = aws_iam_role.enrich_with_metadata_lambda_role.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "lambda_sqs_role_policy" {
  role       = aws_iam_role.enrich_with_metadata_lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaSQSQueueExecutionRole"
}

resource "aws_cloudwatch_log_group" "api_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-stream-api.function_name}"
}

resource "aws_cloudwatch_log_group" "upload_hold_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-upload-event-publisher.function_name}"
}

resource "aws_cloudwatch_log_group" "metadata_enrich_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-metadata-enricher.function_name}"
}
