terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
  backend "s3" {
    bucket = "ssp-sandbox-video-stream-tf-state"
    key    = "tf-state"
    region = "eu-west-2"
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "eu-west-2"
}

locals {
  environmentSuffix = terraform.workspace != "prod" ? "-${terraform.workspace}" : ""
}
