resource "aws_cloudwatch_log_group" "api_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-stream-api.function_name}"
}

resource "aws_cloudwatch_log_group" "upload_hold_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-upload-event-publisher.function_name}"
}

resource "aws_cloudwatch_log_group" "metadata_enrich_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-metadata-enricher.function_name}"
}

resource "aws_cloudwatch_log_group" "api_authorizer_log_group" {
  name = "/aws/lambda/${aws_lambda_function.api_authorizer.function_name}"
}

resource "aws_cloudwatch_log_group" "video-details-updated-event-publisher_log_group" {
  name = "/aws/lambda/${aws_lambda_function.video-details-updated-event-publisher.function_name}"
}

resource "aws_cloudwatch_log_group" "move-video-to-video-store_log_group" {
  name = "/aws/lambda/${aws_lambda_function.move-video-to-video-store.function_name}"
}