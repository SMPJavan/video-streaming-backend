resource "aws_cloudwatch_event_bus" "event_bus" {
  name = "events${local.environmentSuffix}"
}

resource "aws_cloudwatch_event_rule" "video_uploaded_to_metadata_enrich_sqs_rule" {
  name           = "VideoUploadedEvent-to-metadata-enrich-SQS-rule${local.environmentSuffix}"
  event_bus_name = aws_cloudwatch_event_bus.event_bus.arn
  description    = "Dispatches VideoUploadedEvent events to the metadata enrich SQS queue"
  event_pattern = jsonencode({
    detail-type = [
      "VideoUploadedEvent"
    ]
  })
}

resource "aws_cloudwatch_event_rule" "video_updated_to_video_move_sqs_rule" {
  name           = "VideoUpdatedEvent-to-to-move-video-SQS-rule${local.environmentSuffix}"
  event_bus_name = aws_cloudwatch_event_bus.event_bus.arn
  description    = "Dispatches VideoUpdatedEvent events to the video move SQS queue"
  event_pattern = jsonencode({
    detail-type = [
      "VideoDetailsUpdatedEvent"
    ]
  })
}

resource "aws_cloudwatch_event_target" "video_uploaded_to_metadata_enrich_sqs_target" {
  target_id      = "VideoUploadedEvent-to-metadata-enrich-SQS-target${local.environmentSuffix}"
  arn            = aws_sqs_queue.metadata_build_queue.arn
  rule           = aws_cloudwatch_event_rule.video_uploaded_to_metadata_enrich_sqs_rule.name
  event_bus_name = aws_cloudwatch_event_bus.event_bus.arn
}

resource "aws_cloudwatch_event_target" "video_updated_to_video_move_sqs_target" {
  target_id      = "VideoUpdatedEvent-to-move-video-SQS-target${local.environmentSuffix}"
  arn            = aws_sqs_queue.move_video_to_video_store_queue.arn
  rule           = aws_cloudwatch_event_rule.video_updated_to_video_move_sqs_rule.name
  event_bus_name = aws_cloudwatch_event_bus.event_bus.arn
}