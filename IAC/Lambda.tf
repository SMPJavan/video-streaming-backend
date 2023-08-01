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

data "aws_s3_object" "api_authorizer_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "api-authorizer-0.1-all.jar"
}

data "aws_s3_object" "video_details_updated_event_publisher_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "video-details-updated-event-publisher-0.1-all.jar"
}

data "aws_s3_object" "move_video_to_video_store_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "move-video-to-video-store-0.1-all.jar"
}

resource "aws_lambda_function" "video-stream-api" {
  s3_bucket     = data.aws_s3_object.stream_api_package.bucket
  s3_key        = data.aws_s3_object.stream_api_package.key
  function_name = "video-stream-api${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.stream_api_package.version_id

  handler     = "io.micronaut.function.aws.proxy.MicronautLambdaHandler"
  runtime     = "java17"
  timeout     = 30
  memory_size = 256

  role = aws_iam_role.lambda_api_exec.arn

  publish = true

  snap_start {
    apply_on = "PublishedVersions"
  }

  environment {
    variables = {
      DYNAMODB_VIDEO_DETAILS_TABLE_NAME = "video_details${local.environmentSuffix}",
      S3_VIDEOS_BUCKET                  = "ssp-sandbox-video-stream-video-upload-hold${local.environmentSuffix}"
    }
  }
}

resource "aws_lambda_function" "video-upload-event-publisher" {
  s3_bucket     = data.aws_s3_object.stream_video_upload_event_publisher_package.bucket
  s3_key        = data.aws_s3_object.stream_video_upload_event_publisher_package.key
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
  s3_bucket     = data.aws_s3_object.stream_video_enrich_metadata_package.bucket
  s3_key        = data.aws_s3_object.stream_video_enrich_metadata_package.key
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

resource "aws_lambda_function" "video-details-updated-event-publisher" {
  s3_bucket     = data.aws_s3_object.video_details_updated_event_publisher_package.bucket
  s3_key        = data.aws_s3_object.video_details_updated_event_publisher_package.key
  function_name = "video-details-updated-event-publisher${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.video_details_updated_event_publisher_package.version_id

  handler = "ssp.video.stream.FunctionRequestHandler"
  runtime = "java17"
  timeout = 30

  role = aws_iam_role.video_detail_updated_event_publisher_lambda_role.arn

  environment {
    variables = {
      EVENTBRIDGE_BUS_NAME = "events${local.environmentSuffix}"
    }
  }
}

resource "aws_lambda_function" "move-video-to-video-store" {
  s3_bucket     = data.aws_s3_object.move_video_to_video_store_package.bucket
  s3_key        = data.aws_s3_object.move_video_to_video_store_package.key
  function_name = "move-video-to-video-store${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.move_video_to_video_store_package.version_id

  handler = "ssp.video.stream.FunctionRequestHandler"
  runtime = "java17"
  timeout = 900

  role = aws_iam_role.move_video_to_video_store_lambda_role.arn

  environment {
    variables = {
      VIDEO_MOVE_SOURCE_BUCKET_NAME      = "ssp-sandbox-video-stream-video-upload-hold${local.environmentSuffix}"
      VIDEO_MOVE_DESTINATION_BUCKET_NAME = "ssp-sandbox-video-stream-video-store${local.environmentSuffix}"
    }
  }
}

resource "aws_lambda_function" "api_authorizer" {
  s3_bucket     = data.aws_s3_object.api_authorizer_package.bucket
  s3_key        = data.aws_s3_object.api_authorizer_package.key
  function_name = "api-authorizer${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.api_authorizer_package.version_id

  handler = "ssp.video.stream.LambdaAuthorizer"
  runtime = "java17"
  timeout = 30

  role = aws_iam_role.api_authorizer_lambda_role.arn
}

resource "aws_lambda_alias" "video-stream-api_SnapStartAlias" {
  name             = "SnapStart"
  description      = "Alias for SnapStart"
  function_name    = aws_lambda_function.video-stream-api.function_name
  function_version = aws_lambda_function.video-stream-api.version
}

resource "aws_lambda_permission" "apigw" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.video-stream-api.function_name
  principal     = "apigateway.amazonaws.com"
  qualifier     = aws_lambda_alias.video-stream-api_SnapStartAlias.name

  source_arn = "${aws_api_gateway_rest_api.video-stream-api.execution_arn}/*/*"
}

resource "aws_lambda_permission" "allow_bucket" {
  statement_id  = "AllowExecutionFromS3Bucket"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.video-upload-event-publisher.arn
  principal     = "s3.amazonaws.com"
  source_arn    = aws_s3_bucket.upload_hold.arn
}
