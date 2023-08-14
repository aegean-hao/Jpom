#!/usr/bin/bash
#
# The MIT License (MIT)
#
# Copyright (c) 2019 Code Technology Studio
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of
# this software and associated documentation files (the "Software"), to deal in
# the Software without restriction, including without limitation the rights to
# use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
# the Software, and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
# FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
# COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
# IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
# CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#


# Script to deploy certificate to a Gitee hosted page

# The following variables exported from environment will be used.
# If not set then values previously saved in domain.conf file are used.

# All the variables are required
# acme.sh --deploy -d xxx --deploy-hook gitee_pages

# export GITEE_TOKEN="xxx"
# export GITEE_OWNER_ID="xxx"
# export GITEE_REPO_ID="xxx"

gitee_pages_deploy() {
  _cdomain="$1"
  _ckey="$2"
  _ccert="$3"
  _cca="$4"
  _cfullchain="$5"

  _debug _cdomain "$_cdomain"
  _debug _ckey "$_ckey"
  _debug _ccert "$_ccert"
  _debug _cca "$_cca"
  _debug _cfullchain "$_cfullchain"

  if [ -z "$GITEE_TOKEN" ]; then
    if [ -z "$Le_Deploy_gitee_token" ]; then
      _err "GITEE_TOKEN not defined."
      return 1
    fi
  else
    Le_Deploy_gitee_token="$GITEE_TOKEN"
    _savedomainconf Le_Deploy_gitee_token "$Le_Deploy_gitee_token"
  fi

  if [ -z "$GITEE_OWNER_ID" ]; then
    if [ -z "$Le_Deploy_gitee_owner_id" ]; then
      _err "GITEE_OWNER_ID not defined."
      return 1
    fi
  else
    Le_Deploy_gitee_owner_id="$GITEE_OWNER_ID"
    _savedomainconf Le_Deploy_gitee_owner_id "$Le_Deploy_gitee_owner_id"
  fi

  if [ -z "$GITEE_REPO_ID" ]; then
    if [ -z "$Le_Deploy_gitee_repo_id" ]; then
     _err "GITEE_REPO_ID not defined."
     return 1
    fi
  else
    Le_Deploy_gitee_repo_id="$GITEE_REPO_ID"
    _savedomainconf Le_Deploy_gitee_repo_id "$Le_Deploy_gitee_repo_id"
  fi

  string_fullchain=$(_base64 <"$_cfullchain")
  string_key=$(_base64 <"$_ckey")

  body="access_token=$Le_Deploy_gitee_token&ssl_certificate_crt=$string_fullchain&ssl_certificate_key=$string_key&domain=$_cdomain"

  gitee_url="https://gitee.com/api/v5/repos/$Le_Deploy_gitee_owner_id/$Le_Deploy_gitee_repo_id/pages"

  _response=$(_post "$body" "$gitee_url" 0 PUT | _dbase64)

  error_response="error"

  if test "${_response#*$error_response}" != "$_response"; then
    _err "Error in deploying certificate:"
    _err "$_response"
    return 1
  fi

  _debug response "$_response"

  body="access_token=$Le_Deploy_gitee_token"
  gitee_url="https://gitee.com/api/v5/repos/$Le_Deploy_gitee_owner_id/$Le_Deploy_gitee_repo_id/pages/builds"

  _response=$(_post "$body" "$gitee_url" 0 POST | _dbase64)

  error_response="error"

  if test "${_response#*$error_response}" != "$_response"; then
    _err "Error in deploying builds certificate:"
    _err "$_response"
    return 1
  fi
  _debug response "$_response"

  _info "Certificate successfully deployed"

  return 0
}
