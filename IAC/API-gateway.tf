resource "aws_api_gateway_rest_api" "video-stream-api" {
  name        = "video-stream-api${local.environmentSuffix}"
  description = "Proxy to handle requests to the video streaming API"
}

resource "aws_api_gateway_authorizer" "authorizer" {
  name                   = "custom_authorizer"
  rest_api_id            = aws_api_gateway_rest_api.video-stream-api.id
  authorizer_uri         = aws_lambda_function.api_authorizer.invoke_arn
  authorizer_credentials = aws_iam_role.api_authorizer_api_gw_role.arn
  type                   = "TOKEN"
}

resource "aws_api_gateway_resource" "videos_base_resource" {
  parent_id   = aws_api_gateway_rest_api.video-stream-api.root_resource_id
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  path_part   = "videos"
}

resource "aws_api_gateway_resource" "video_details_resource" {
  parent_id   = aws_api_gateway_resource.videos_base_resource.id
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  path_part   = "{videoId}"
}

resource "aws_api_gateway_method" "get_video_details_method" {
  rest_api_id   = aws_api_gateway_rest_api.video-stream-api.id
  resource_id   = aws_api_gateway_resource.video_details_resource.id
  http_method   = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_video_details_integration" {
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  resource_id = aws_api_gateway_method.get_video_details_method.resource_id
  http_method = aws_api_gateway_method.get_video_details_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_alias.video-stream-api_SnapStartAlias.invoke_arn
}

resource "aws_api_gateway_method" "post_video_details_method" {
  rest_api_id   = aws_api_gateway_rest_api.video-stream-api.id
  resource_id   = aws_api_gateway_resource.videos_base_resource.id
  http_method   = "POST"
  authorization = "CUSTOM"
  authorizer_id = aws_api_gateway_authorizer.authorizer.id
}

resource "aws_api_gateway_integration" "post_video_details_integration" {
  rest_api_id = aws_api_gateway_rest_api.video-stream-api.id
  resource_id = aws_api_gateway_method.post_video_details_method.resource_id
  http_method = aws_api_gateway_method.post_video_details_method.http_method

  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_alias.video-stream-api_SnapStartAlias.invoke_arn
}

resource "aws_api_gateway_deployment" "video-stream-api" {
  rest_api_id       = aws_api_gateway_rest_api.video-stream-api.id
  stage_name        = "default"
  stage_description = md5(file("API-gateway.tf"))
  depends_on        = [aws_api_gateway_rest_api.video-stream-api]

  lifecycle {
    create_before_destroy = true
  }
}

output "base_url" {
  value = aws_api_gateway_deployment.video-stream-api.invoke_url
}