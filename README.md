# Spring-Boot Camel XA Transactions Quickstart

## Updated for Oracle DB

When this example is run with:

        mvn clean install && java -javaagent:$BYTEMAN_HOME/lib/byteman.jar=script:$BYTEMAN_RULES/jta_transaction.btm,script:$BYTEMAN_RULES/jta_xaresource.btm,script:$BYTEMAN_RULES/jms_xaconnectionfactory.btm  -Ddatabase.host=localhost -Ddatabase.username=admin -Ddatabase.password=admin -Dbroker.host=localhost -Dbroker.username=admin -Dbroker.password=admin -jar target/spring-boot-camel-xa-1.0.0.fuse-740017-redhat-00001.jar

You should see the following Byteman output in the logs - showing **commit** actions on all 3 `XAResource`s:

```
***** JTA Transaction#init                     (TransactionImple < ac, NoTransaction >)
***** JTA Transaction#registerSynchronization  (TransactionImple < ac, BasicAction: 0:ffffc0a801ea:85d3:5de3ea97:21 status: ActionStatus.RUNNING >,sync=org.messaginghub.pooled.jms.pool.PooledXAConnection$Synchronization@14f834b1)
***** JTA Transaction#enlistResource           (TransactionImple < ac, BasicAction: 0:ffffc0a801ea:85d3:5de3ea97:21 status: ActionStatus.RUNNING >,xaRes=oracle.jms.AQjmsXAResource@1c4bc8d6)
***** JTA-XA XAResource#setTransactionTimeout  (oracle.jms.AQjmsXAResource@1c4bc8d6;seconds=60)
***** JTA-XA XAResource#setTransactionTimeout  (oracle.jdbc.driver.T4CXAResource@7fd2f093;seconds=60)
***** JTA-XA XAResource#start                  (oracle.jms.AQjmsXAResource@1c4bc8d6;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:23, subordinatenodename=null, eis_name=0 >,flags=0)
2019-12-01 16:30:26.774  INFO 25735 --- [sumer[FOOQUEUE]] route4                                   : Message sent to outbound: TEST JAVA
***** JTA-XA XAResource#isSameRM               (oracle.jms.AQjmsXAResource@1c4bc8d6;xares=org.postgresql.xa.PGXAConnection@935d3f9)
***** JTA-XA XAResource#isSameRM               (oracle.jdbc.driver.T4CXAResource@7fd2f093;xares=org.postgresql.xa.PGXAConnection@935d3f9)
***** JTA-XA XAResource#setTransactionTimeout  (org.postgresql.xa.PGXAConnection@935d3f9;seconds=60)
***** JTA-XA XAResource#start                  (org.postgresql.xa.PGXAConnection@935d3f9;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:26, subordinatenodename=null, eis_name=0 >,flags=0)
***** JMS-XA XAConnectionFactory#createXAConnection     (org.apache.activemq.ActiveMQXAConnectionFactory@1d239476)
***** JTA-XA XAResource#init                   (TransactionContext{transactionId=null,connection=null})
***** JTA Transaction#enlistResource           (TransactionImple < ac, BasicAction: 0:ffffc0a801ea:85d3:5de3ea97:21 status: ActionStatus.RUNNING >,xaRes=TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}})
***** JTA-XA XAResource#isSameRM               (org.postgresql.xa.PGXAConnection@935d3f9;xares=TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}})
***** JTA-XA XAResource#isSameRM               (oracle.jms.AQjmsXAResource@1c4bc8d6;xares=TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}})
***** JTA-XA XAResource#isSameRM               (oracle.jdbc.driver.T4CXAResource@7fd2f093;xares=TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}})
***** JTA-XA XAResource#setTransactionTimeout  (TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}};seconds=60)
***** JTA-XA XAResource#start                  (TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}};Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:2a, subordinatenodename=null, eis_name=0 >,flags=0)
***** JTA Transaction#delistResource           (TransactionImple < ac, BasicAction: 0:ffffc0a801ea:85d3:5de3ea97:21 status: ActionStatus.RUNNING >,xaRes=org.postgresql.xa.PGXAConnection@935d3f9,flag=67108864)
***** JTA-XA XAResource#end                    (org.postgresql.xa.PGXAConnection@935d3f9;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:26, subordinatenodename=null, eis_name=0 >,int=67108864)
***** JTA-XA XAResource#end                    (oracle.jms.AQjmsXAResource@1c4bc8d6;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:23, subordinatenodename=null, eis_name=0 >,int=67108864)
***** JTA-XA XAResource#prepare                (oracle.jms.AQjmsXAResource@1c4bc8d6;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:23, subordinatenodename=null, eis_name=0 >)
***** JTA-XA XAResource#prepare                (org.postgresql.xa.PGXAConnection@935d3f9;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:26, subordinatenodename=null, eis_name=0 >)
***** JTA-XA XAResource#end                    (TransactionContext{transactionId=XID:[131077,globalId=0:ffffc0a801ea:85d3:5de3ea97:21,branchId=0:ffffc0a801ea:85d3:5de3ea97:2a],connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}};Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:2a, subordinatenodename=null, eis_name=0 >,int=67108864)
***** JTA-XA XAResource#prepare                (TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}};Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:2a, subordinatenodename=null, eis_name=0 >)
***** JTA-XA XAResource#commit                 (oracle.jms.AQjmsXAResource@1c4bc8d6;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:23, subordinatenodename=null, eis_name=0 >,onePhase=false)
***** JTA-XA XAResource#commit                 (org.postgresql.xa.PGXAConnection@935d3f9;Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:26, subordinatenodename=null, eis_name=0 >,onePhase=false)
***** JTA-XA XAResource#commit                 (TransactionContext{transactionId=null,connection=ActiveMQConnection {id=ID:tdonohue-f29-45897-1575217826893-1:1,clientId=ID:tdonohue-f29-45897-1575217826893-0:1,started=false}};Xid=< formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a801ea:85d3:5de3ea97:21, node_name=1, branch_uid=0:ffffc0a801ea:85d3:5de3ea97:2a, subordinatenodename=null, eis_name=0 >,onePhase=false)
```


----

This example demonstrates how to run a Camel Service on Spring-Boot that supports XA transactions on two external transactional resources: a JMS resource (A-MQ) and a database (PostgreSQL).

External resources can be provided by Openshift and must be started before running this quickstart.  

The quickstart uses Openshift `StatefulSet` resources to guarantee uniqueness of transaction managers and 
require a `PersistentVolume` to store transaction logs.

The application **supports scaling** on the `StatefulSet` resource. Each instance will have its own "in process" recovery manager.

A special controller guarantees that when the application is scaled down, all instances that are terminated complete correctly all their work without
leaving pending transactions. The scale-down operation is rolled back by the controller if the recovery manager has not been
able to flush all pending work before terminating.

In order for the recovery controller to work, `edit` permissions on the current namespace are required (role binding is included in the set of resources published to Openshift).
The recovery controller can be disabled using the `CLUSTER_RECOVERY_ENABLED` environment variable: in that case, no special permissions are required on the service account but 
any scale-down operation may leave pending transactions on the terminated pod without notice. 

All commands below requires one of these:
- be logged in to the targeted OpenShift instance (using oc login command line tool for instance)
- configure properties to specify to which OpenShift instance it should connect

IMPORTANT: This quickstart can run in 2 modes: standalone on your machine and on your Single-node OpenShift Cluster 

### Building

The example can be built with

    mvn clean install
    
### Running the Quickstart standalone on your machine

You can also run this booster as a standalone project directly.

Obtain the project and enter the project's directory.

Set the `database.*` and `broker.*` properties in `src/main/resources/application.properties` to point 
to running instances of a Postgresql database and a A-MQ broker.

Build the project:

    mvn clean package
    mvn spring-boot:run 

### Running the Quickstart on a Single-node OpenShift Cluster

If you have a single-node OpenShift cluster, such as Minishift or the Red Hat Container Development Kit, link:http://appdev.openshift.io/docs/minishift-installation.html[installed and running], you can also deploy your booster there. A single-node OpenShift cluster provides you with access to a cloud environment that is similar to a production environment.

To deploy your booster to a running single-node OpenShift cluster:

Log in and create your project:

    oc login -u developer -p developer
    oc new-project MY_PROJECT_NAME

Install dependencies:
- From the Openshift catalog install `postgresql` using `theuser` as username and `Thepassword1!` as password
- From the Openshift catalog install a `A-MQ` broker using `theuser` as username and `Thepassword1!` as password

Change the Postgresql database to accept prepared statements:

    
    oc env dc/postgresql POSTGRESQL_MAX_PREPARED_TRANSACTIONS=100


Create a persistent volume claim for the transaction log:

    oc create -f persistent-volume-claim.yml

Build and deploy your booster:

    mvn clean fabric8:deploy -Popenshift

Scale it up to the desired number of replicas:

    oc scale statefulset spring-boot-camel-xa --replicas 3

### Caveats

The pod name is used as transaction manager id (`spring.jta.transaction-manager-id` property). The current implementation also 
limits the length of transaction manager ids.
So, you must be aware of the following:

- The name of the statefulset is an identifier for the transaction system, so it must not be changed
- You should name the statefulset so that all of its pod names have length **lower than or equal to 23 characters**

Pod names are created by Openshift using the convention: `<statefulset-name>-0`, `<statefulset-name>-1` and so on.

Note that Narayana does its best to avoid having multiple recovery managers with the same id, so when the pod name is longer than the 
limit, the *last 23 bytes* are taken as transaction manager id (after stripping some characters like `-`).

### Using the Quickstart

Once the quickstart is running you can get the base service URL using the following command:


For Openshift:

    NARAYANA_HOST=$(oc get route spring-boot-camel-xa -o jsonpath={.spec.host})

For standalone installation:

    NARAYANA_HOST=localhost:8080

The application exposes the following rest URLs:

- GET on `http://$NARAYANA_HOST/api/`: list all messages in the `audit_log` table (ordered)
- POST on `http://$NARAYANA_HOST/api/?entry=xxx`: put a message `xxx` in the `incoming` queue for processing

#### Simple workflow

First get a list of messages in the `audit_log` table:

```
curl -w "\n" http://$NARAYANA_HOST/api/
```

The list should be empty at the beginning. Now you can put the first element.

```
curl -w "\n" -X POST http://$NARAYANA_HOST/api/?entry=hello
# wait a bit
curl -w "\n" http://$NARAYANA_HOST/api/
```

The new list should contain two messages: `hello` and `hello-ok`.

The `hello-ok` confirms that the message has been sent to a `outgoing` queue and then logged.
 
You can add multiple messages and see the logs. The following actions force the application in some corner cases 
to examine the behavior.

#### Exception handling

Send a message named `fail`:

```
curl -w "\n" -X POST http://$NARAYANA_HOST/api/?entry=fail
# wait a bit
curl -w "\n" http://$NARAYANA_HOST/api/
```

This message produces an exception at the end of the route, so that the transaction is always rolled back.

You should **not** find any trace of the message in the `audit_log` table.
