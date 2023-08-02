

resource "aws_iam_role_policy_attachment" "api_put_dynamodb_policy_attachment" {
  role       = aws_iam_role.lambda_api_exec.id
  policy_arn = aws_iam_policy.lambda_put_dynamodb_policy.arn
}

resource "aws_iam_role_policy_attachment" "enrich_metadata_put_dynamodb_policy_attachment" {
  role       = aws_iam_role.enrich_with_metadata_lambda_role.id
  policy_arn = aws_iam_policy.lambda_put_dynamodb_policy.arn
}

resource "aws_iam_role_policy_attachment" "enrich_metadata_s3_policy_attachment" {
  role       = aws_iam_role.enrich_with_metadata_lambda_role.id
  policy_arn = aws_iam_policy.lambda_s3_policy.arn
}

resource "aws_iam_role_policy_attachment" "api_s3_signed_url_policy_attachment" {
  role       = aws_iam_role.lambda_api_exec.id
  policy_arn = aws_iam_policy.lambda_s3_policy.arn
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

resource "aws_iam_role_policy_attachment" "api_authorizer_logging_policy_attachment" {
  role       = aws_iam_role.api_authorizer_lambda_role.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "api_authorizer_lambda_policy_attachment" {
  role       = aws_iam_role.api_authorizer_api_gw_role.id
  policy_arn = aws_iam_policy.lambda_invoke_policy.arn
}

resource "aws_iam_role_policy_attachment" "video_details_dynamodb_iam_for_lambda_policy_attachment" {
  role       = aws_iam_role.video_details_dynamodb_iam_for_lambda.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "video_details_updated_put_events_policy_attachment" {
  role       = aws_iam_role.video_detail_updated_event_publisher_lambda_role.id
  policy_arn = aws_iam_policy.lambda_put_events_policy.arn
}

resource "aws_iam_role_policy_attachment" "video_details_updated_stream_dynamodb_policy_attachment" {
  role       = aws_iam_role.video_detail_updated_event_publisher_lambda_role.id
  policy_arn = aws_iam_policy.lambda_dynamodb_streams_policy.arn
}

resource "aws_iam_role_policy_attachment" "video_details_updated_logging_lambda_policy_attachment" {
  role       = aws_iam_role.video_detail_updated_event_publisher_lambda_role.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "move_video_to_video_store_logging_policy_attachment" {
  role       = aws_iam_role.move_video_to_video_store_lambda_role.id
  policy_arn = aws_iam_policy.function_logging_policy.arn
}

resource "aws_iam_role_policy_attachment" "move_video_to_video_store_s3_policy_attachment" {
  role       = aws_iam_role.move_video_to_video_store_lambda_role.id
  policy_arn = aws_iam_policy.lambda_s3_policy.arn
}

resource "aws_iam_role_policy_attachment" "move_video_to_video_store_dynamodb_policy_attachment" {
  role       = aws_iam_role.move_video_to_video_store_lambda_role.id
  policy_arn = aws_iam_policy.lambda_put_dynamodb_policy.arn
}

resource "aws_iam_role_policy_attachment" "move_video_to_video_store_lambda_sqs_role_policy" {
  role       = aws_iam_role.move_video_to_video_store_lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaSQSQueueExecutionRole"
}