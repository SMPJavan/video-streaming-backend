resource "aws_sqs_queue" "metadata_build_queue" {
  name                      = "video-stream-metadata-enrich-queue${local.environmentSuffix}"
}

resource "aws_sqs_queue_policy" "metadata_build_queue_policy" {
  policy = data.aws_iam_policy_document.eventbridge_publish_to_sqs_policy.json
  queue_url = aws_sqs_queue.metadata_build_queue.id
}

data "aws_iam_policy_document" "eventbridge_publish_to_sqs_policy" {
  statement {
    effect  = "Allow"
    actions = ["sqs:SendMessage"]

    principals {
      type        = "Service"
      identifiers = ["events.amazonaws.com"]
    }

    resources = [aws_sqs_queue.metadata_build_queue.arn]
  }
}