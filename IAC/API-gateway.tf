resource "aws_api_gateway_rest_api" "video-stream-api" {
  name = "video-stream-api${local.environmentSuffix}"
  description = "Proxy to handle requests to the video streaming API"
}

resource "aws_api_gateway_resource" "hello_root" {
  parent_id   = aws_api_gateway_rest_api.video-stream-api.root_resource_id
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  path_part   = "{.+}"
}

resource "aws_api_gateway_method" "hello_root" {
  rest_api_id   = aws_api_gateway_rest_api.video-stream-api.id
  resource_id   = aws_api_gateway_resource.hello_root.id
  http_method   = "ANY"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_root" {
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  resource_id = aws_api_gateway_method.hello_root.resource_id
  http_method = aws_api_gateway_method.hello_root.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.video-stream-api.invoke_arn
}

resource "aws_api_gateway_deployment" "video-stream-api" {
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  stage_name = "default"
}

output "base_url" {
  value = aws_api_gateway_deployment.video-stream-api.invoke_url
}