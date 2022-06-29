{{/*
Expand the name of the chart.
*/}}
{{- define "mockexchange-common.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "mockexchange-common.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "mockexchange-common.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "mockexchange-common.labels" -}}
helm.sh/chart: {{ include "mockexchange-common.chart" . }}
{{ include "mockexchange-common.selectorLabels" . }}
{{/*{{- if .Chart.AppVersion }}*/}}
{{/*app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}*/}}
{{/*version: {{ .Chart.AppVersion | quote }}*/}}
{{/*{{- end }}*/}}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "mockexchange-common.selectorLabels" -}}
app.kubernetes.io/name: {{ include "mockexchange-common.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app: {{ include "mockexchange-common.name" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "mockexchange-common.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "mockexchange-common.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create the name of the database secret to use
*/}}
{{- define "mockexchange-common.datbaseSecretName" -}}
{{- if .Values.database.secret.create }}
{{- default (printf "%s-database" (include "mockexchange-common.fullname" .)) .Values.database.secret.name }}
{{- else }}
{{- default "default" .Values.database.secret.name }}
{{- end }}
{{- end }}

{{/*
Create the name of the redis secret to use
*/}}
{{- define "mockexchange-common.redisSecretName" -}}
{{- if .Values.redis.secret.create }}
{{- default (printf "%s-redis" (include "mockexchange-common.fullname" .)) .Values.redis.secret.name }}
{{- else }}
{{- default "default" .Values.redis.secret.name }}
{{- end }}
{{- end }}

{{/*
Create the name of the oauth secret to use
*/}}
{{- define "mockexchange-common.oauthSecretName" -}}
{{- if .Values.oauth.secret.create }}
{{- default (printf "%s-oauth" (include "mockexchange-common.fullname" .)) .Values.oauth.secret.name }}
{{- else }}
{{- default "default" .Values.oauth.secret.name }}
{{- end }}
{{- end }}

{{- /*
mylibchart.util.merge will merge two YAML templates and output the result.
This takes an array of three values:
- the top context
- the template name of the overrides (destination)
- the template name of the base (source)
*/}}
{{- define "mockexchange-common.util.merge" -}}
{{- $top := first . -}}
{{- $overrides := fromYaml (include (index . 1) $top) | default (dict ) -}}
{{- $tpl := fromYaml (include (index . 2) $top) | default (dict ) -}}
{{- toYaml (merge $overrides $tpl) -}}
{{- end -}}


{{- define "mockexchange-common.image" -}}
{{- with .Values.image -}}
{{- $image := printf "%s:%s" .repository (.tag | default "latest") }}
{{- if .registry }}
{{- .registry }}/{{ $image }}
{{- else }}
{{- $image }}
{{- end }}
{{- end }}
{{- end }}
