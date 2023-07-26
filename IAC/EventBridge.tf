resource "aws_cloudwatch_event_bus" "event_bus" {
  name = "events${local.environmentSuffix}"
}