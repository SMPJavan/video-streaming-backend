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

resource "aws_s3_bucket_policy" "cdn-cf-policy" {
  bucket = aws_s3_bucket.video_store.id
  policy = data.aws_iam_policy_document.video-store-cdn-cf-policy.json
}

data "aws_iam_policy_document" "video-store-cdn-cf-policy" {
  statement {
    sid = "1"
    principals {
      type        = "Service"
      identifiers = ["cloudfront.amazonaws.com"]
    }

    actions = [
      "s3:GetObject"
    ]

    resources = [
      "${aws_s3_bucket.video_store.arn}/*"
    ]
  }
}