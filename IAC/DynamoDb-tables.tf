resource "aws_dynamodb_table" "video-details" {
  name             = "video_details${local.environmentSuffix}"
  billing_mode     = "PROVISIONED"
  read_capacity    = "30"
  write_capacity   = "30"
  stream_enabled   = true
  stream_view_type = "NEW_AND_OLD_IMAGES"
  attribute {
    name = "videoId"
    type = "S"
  }
  hash_key = "videoId"
}

resource "aws_lambda_event_source_mapping" "video-details_event_source_mapping" {
  event_source_arn  = aws_dynamodb_table.video-details.stream_arn
  function_name     = aws_lambda_function.video-details-updated-event-publisher.arn
  starting_position = "LATEST"
}