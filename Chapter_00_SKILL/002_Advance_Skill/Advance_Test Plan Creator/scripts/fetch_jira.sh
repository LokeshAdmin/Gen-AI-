#!/usr/bin/env bash
set -euo pipefail

usage() {
  printf 'Usage: %s <JIRA-KEY>\n' "$0" >&2
  printf 'Set JIRA_BASE_URL, JIRA_EMAIL, and JIRA_API_TOKEN before running.\n' >&2
}

if [[ $# -eq 1 && ( "$1" == "-h" || "$1" == "--help" ) ]]; then
  usage
  exit 0
fi

if [[ $# -ne 1 ]]; then
  usage
  exit 2
fi

issue_key=$1
if [[ ! "$issue_key" =~ ^[A-Za-z][A-Za-z0-9_]*-[0-9]+$ ]]; then
  printf 'Error: invalid Jira issue key: %s\n' "$issue_key" >&2
  usage
  exit 2
fi

: "${JIRA_BASE_URL:?Set JIRA_BASE_URL to your Jira HTTPS URL}"
: "${JIRA_EMAIL:?Set JIRA_EMAIL to your Jira account email}"
: "${JIRA_API_TOKEN:?Set JIRA_API_TOKEN to your Jira API token}"

case "$JIRA_BASE_URL" in
  https://*) ;;
  *)
    printf 'Error: JIRA_BASE_URL must use HTTPS.\n' >&2
    exit 2
    ;;
esac

base_url=${JIRA_BASE_URL%/}

exec curl \
  --silent \
  --show-error \
  --fail-with-body \
  --request GET \
  --url "${base_url}/rest/api/3/issue/${issue_key}" \
  --user "${JIRA_EMAIL}:${JIRA_API_TOKEN}" \
  --header 'Accept: application/json'
