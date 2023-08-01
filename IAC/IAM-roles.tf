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

resource "aws_iam_role" "upload_hold_iam_for_lambda" {
  name               = "upload_hold_iam_for_lambda${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.upload_hold-iam-policy.json
}

resource "aws_iam_role" "enrich_with_metadata_lambda_role" {
  name               = "EnrichMetadataLambdaRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_lambda_role_iam_policy.json
}

resource "aws_iam_role" "video_detail_updated_event_publisher_lambda_role" {
  name               = "VideoDetailsUpdatedEventPublisherLambdaRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_lambda_role_iam_policy.json
}

resource "aws_iam_role" "api_authorizer_api_gw_role" {
  name               = "APIAuthorizerAPIGWRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_api_gw_role_iam_policy.json
}

resource "aws_iam_role" "api_authorizer_lambda_role" {
  name               = "APIAuthorizerLambdaRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_lambda_role_iam_policy.json
}

resource "aws_iam_role" "video_details_dynamodb_iam_for_lambda" {
  name               = "video_details_dynamodb_iam_for_lambda${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_lambda_role_iam_policy.json
}

resource "aws_iam_role" "move_video_to_video_store_lambda_role" {
  name               = "MoveVideoToVideoStoreLambdaRole${local.environmentSuffix}"
  assume_role_policy = data.aws_iam_policy_document.assume_lambda_role_iam_policy.json
}
