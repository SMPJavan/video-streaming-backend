resource "aws_sqs_queue" "metadata_build_queue" {
  name = "video-stream-metadata-enrich-queue${local.environmentSuffix}"
}

resource "aws_sqs_queue" "move_video_to_video_store_queue" {
  name                       = "move-video-to-video-store-queue${local.environmentSuffix}"
  visibility_timeout_seconds = 900
}

resource "aws_sqs_queue_policy" "metadata_build_queue_policy" {
  policy    = data.aws_iam_policy_document.eventbridge_publish_to_sqs_policy.json
  queue_url = aws_sqs_queue.metadata_build_queue.id
}

resource "aws_sqs_queue_policy" "move_video_to_video_store_queue_policy" {
  policy    = data.aws_iam_policy_document.eventbridge_publish_to_sqs_policy.json
  queue_url = aws_sqs_queue.move_video_to_video_store_queue.id
}

resource "aws_lambda_event_source_mapping" "metadata_build_event_source_mapping" {
  event_source_arn = aws_sqs_queue.metadata_build_queue.arn
  function_name    = aws_lambda_function.video-metadata-enricher.arn
}

resource "aws_lambda_event_source_mapping" "move_video_event_source_mapping" {
  event_source_arn = aws_sqs_queue.move_video_to_video_store_queue.arn
  function_name    = aws_lambda_function.move-video-to-video-store.arn
}