resource "aws_dynamodb_table" "video-details" {
  name = "video_details${local.environmentSuffix}"
  billing_mode = "PROVISIONED"
  read_capacity= "30"
  write_capacity= "30"
  attribute {
    name = "videoId"
    type = "S"
  }
  hash_key = "videoId"
}