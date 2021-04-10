#!/bin/bash

export KUBECONFIG=$(pwd)/openshift-install-dir/auth/kubeconfig

oc create -f nfs.yaml
oc get pv
