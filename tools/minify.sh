#!/bin/sh

cd $(dirname $0)/../web

<<<<<<< HEAD
SDK="../../ext-6.0.0"
=======
SDK="../../ext-6.0.1"
>>>>>>> 191caf45e9e42fa2ec43d6ec756bb7212ecaca8d

sencha compile --classpath=app.js,app,$SDK/packages/core/src,$SDK/packages/core/overrides,$SDK/classic/classic/src,$SDK/classic/classic/overrides \
       exclude -all \
       and \
       include -recursive -file app.js \
       and \
       exclude -namespace=Ext \
       and \
       concatenate -closure app.min.js
