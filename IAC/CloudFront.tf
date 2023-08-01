
locals {
  s3_origin_id = "video-stream-cloudfront-dist${local.environmentSuffix}"
}

resource "aws_cloudfront_origin_access_control" "cloudfront_s3_video_store_oac" {
  name                              = "CloudFront S3 Video Store OAC ${terraform.workspace}"
  description                       = "Cloud Front S3 Video Store OAC ${terraform.workspace}"
  origin_access_control_origin_type = "s3"
  signing_behavior                  = "always"
  signing_protocol                  = "sigv4"
}

resource "aws_cloudfront_distribution" "s3_distribution" {
  origin {

    domain_name              = aws_s3_bucket.video_store.bucket_regional_domain_name
    origin_id                = local.s3_origin_id
    origin_access_control_id = aws_cloudfront_origin_access_control.cloudfront_s3_video_store_oac.id
  }


  enabled         = true
  is_ipv6_enabled = true
  comment         = "video streaming S3 CloudFront distribution for env: ${terraform.workspace}"



  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = local.s3_origin_id

    forwarded_values {
      query_string = false

      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "allow-all"
    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400
  }

  # Cache behavior with precedence 0
  ordered_cache_behavior {
    path_pattern     = "/content/immutable/*"
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD", "OPTIONS"]
    target_origin_id = local.s3_origin_id

    forwarded_values {
      query_string = false
      headers      = ["Origin"]

      cookies {
        forward = "none"
      }
    }

    min_ttl                = 0
    default_ttl            = 86400
    max_ttl                = 31536000
    compress               = true
    viewer_protocol_policy = "redirect-to-https"
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  # Cache behavior with precedence 1
  ordered_cache_behavior {
    path_pattern     = "/content/*"
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = local.s3_origin_id

    forwarded_values {
      query_string = true

      cookies {
        forward = "none"
      }
    }

    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400
    compress               = true
    viewer_protocol_policy = "redirect-to-https"
  }

  tags = {
    Environment = terraform.workspace
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }
}