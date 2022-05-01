{{/*
Create the name of the oauth secret to use
*/}}
{{- define "mockexchange-api.tlsSecretName" -}}
{{- default (printf "%s-tls" (include "mockexchange-common.fullname" .)) .Values.ingress.tls.secretName }}
{{- end }}

{{/*
Create the name of the oauth secret to use
*/}}
{{- define "mockexchange-api.host" -}}
{{ printf "%s.%s" (include "mockexchange-common.fullname" .) .Values.ingress.domain }}
{{- end }}
