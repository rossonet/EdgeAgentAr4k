#!/bin/bash
#oc --kubeconfig=openshift-install-dir/auth/kubeconfig get pod -A

export KUBECONFIG=$(pwd)/openshift-install-dir/auth/kubeconfig

oc --kubeconfig=openshift-install-dir/auth/kubeconfig get csr -ojson | jq -r '.items[] | select(.status == {} ) | .metadata.name' | xargs oc --kubeconfig=openshift-install-dir/auth/kubeconfig adm certificate approve
