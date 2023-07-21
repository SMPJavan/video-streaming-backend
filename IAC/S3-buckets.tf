resource "aws_s3_bucket" "video_store" {
  bucket   = "ssp-sandbox-video-stream-video-store${local.environmentSuffix}"
}

resource "aws_s3_bucket_acl" "video_store" {
  bucket = aws_s3_bucket.video_store.id
  acl    = "private"
}

resource "aws_s3_bucket_ownership_controls" "s3_bucket_acl_ownership" {
  bucket = aws_s3_bucket.video_store.id
  rule {
    object_ownership = "ObjectWriter"
  }
}