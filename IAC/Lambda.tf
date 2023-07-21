data "aws_s3_object" "stream_api_package" {
  bucket = "ssp-sandbox-video-stream-apps-store"
  key    = "api-0.1-all.jar"
}

resource "aws_lambda_function" "video-stream-api" {
  s3_bucket        = "ssp-sandbox-video-stream-apps-store"
  s3_key           = "api-0.1-all.jar"
  function_name = "video-stream-api${local.environmentSuffix}"

  source_code_hash = data.aws_s3_object.stream_api_package.version_id

  handler = "io.micronaut.function.aws.proxy.MicronautLambdaHandler"
  runtime = "java17"
  timeout = 30

  role = aws_iam_role.lambda_api_exec.arn
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

resource "aws_lambda_permission" "apigw" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.video-stream-api.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_api_gateway_rest_api.video-stream-api.execution_arn}/*/*"
}