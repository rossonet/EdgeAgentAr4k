#!/bin/bash

export KUBECONFIG=$(pwd)/openshift-install-dir/auth/kubeconfig

htpasswd -c -B -b users.htpasswd rossonet $(cat openshift-install-dir/auth/kubeadmin-password)
htpasswd -B -b users.htpasswd staer $(cat openshift-install-dir/auth/kubeadmin-password)
htpasswd -B -b users.htpasswd ospite1 $(cat openshift-install-dir/auth/kubeadmin-password)
htpasswd -B -b users.htpasswd ospite2 $(cat openshift-install-dir/auth/kubeadmin-password)
htpasswd -B -b users.htpasswd ospite3 $(cat openshift-install-dir/auth/kubeadmin-password)
htpasswd -B -b users.htpasswd redev $(cat openshift-install-dir/auth/kubeadmin-password)

oc create secret generic htpass-secret --from-file=htpasswd=users.htpasswd -n openshift-config
oc apply -f htpasswd_provider.yaml
oc adm policy add-cluster-role-to-user cluster-admin rossonet
