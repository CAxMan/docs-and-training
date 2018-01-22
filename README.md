# CloudFlow platform training

Welcome to the CloudFlow platform training! In this training session, you will
learn how to use the CloudFlow platform to deploy and modify web services and
use these in automatically executed workflows.

## Prerequisites
### Required software
You will be executing all steps either on a website or in a remote session on
a virtual machine we set up for this training session. You will thus only need
a current web browser and an SSH client. On Windows, PuTTY is a good choice,
since it also is available as a portable version which does not need to be
installed. For users who are already using Git for Windows, the git bash
comes with a bundled ssh client which is just as fine.

### Addresses and passwords
The training will take place on two locations:
1. The CloudFlow portal, available under https://api.hetcomp.org
2. A dedicated training virtual machine, available under training.hetcomp.org or 35.159.5.150.

Your username and password will be handed out to you on paper.

## Preface: CloudFlow deployment strategy
The CloudFlow platform is hosted on Amazon Web Service and relies on Docker
images for deployment. A Docker image encapsulates an arbitrary software
environment and makes this environment independent of the host system. A Docker
image can, for example, contain a Linux operating system with an already
configured web server which is ready to host a website. Or it could contain a
computation environment for finite-element calculations. To execute such an
image, all one needs is a running Docker installation, but none of the actual
software contained inside the image. This makes Docker ideal for developing
services in one place and deploying them, possibly more than once, somewhere
else.

In this training session, you will build and modify Docker images, which will
then be deployed and made available on the CloudFlow platform.

## Tutorial 1: Local building and testing of a synchronous service
Before deploying a new or modified web service on the CloudFlow platform, it is
wise to build and test the service locally.

Note: In this training session, "locally" actually means inside the training
VM. Log into the training VM with your SSH client now. We have created some
simple example services which you will build and modify.

1. Enter the tutorial directory by typing:
   ```
   $ cd CloudiFacturingTraining/synchronous_service/netbeans_project
   ```

2. Compile the Java source code using maven (`mvn` on the command line), the
   Java build tool:
   ```
   $ mvn package
   ```
   This will create the folder `./target` and the file
   `cloudflow_training_-1.0-SNAPSHOT.war` inside it. This .war file contains 
   the compiled application, and it is this file we will deploy inside a
   Docker container.

3. Edit Dockerfile and docker-entrypoint.sh and change the target name

   The name of the .war file _inisde the container_ will determine the URL path
   under which the service is available. (It will be, in the end, 
   `https://srv.hetcomp.org/<war-file-name>/...`.) To avoid conflicting service
   names (everyone in the training session starts with the same files), we need
   to rename this file.

   Therefore, first edit `Dockerfile` and change line number 3 to the following:
   ```
   COPY target/cloudflow_training-1.0-SNAPSHOT.war /ct-sync-<newname>.war
   ```
   Replace `<newname>` with a unique identifier, for example your username.
   Make sure _not_ to use underscores or capital letters in this name, as they
   will later interfere with the Amazon Web Services tools. Also choose a name
   as short as possible, since Amazon also imposes restrictions on that.


   Then, edit `docker-entrypoint.sh` and similarly change line 27 to:
   ```
   asadmin -u admin -W /tmp/glassfishpwd deploy /ct-sync-<newname>.war
   ```

   From now on, the full `ct-syncservice-<newname>` text will be 
   referred to as the `<servicename>` of your service.

4. Now, build the Docker image:
   ```
   $ docker build -t <servicename> .
   ```
   This creates a Docker image and stores it locally.
   `<servicename>` should be the same full name as chosen in the previous step.

5. Run the Docker image as a local container
   ```
   $ docker run -p 8080:8080 --env-file=env_template <servicename>
   ```
   * The `-p` parameter maps ports from the host (= the training VM) to the
     container instance. In this case, it is port 8080, which is the port
     glassfish (the Java application server contained in the Docker image) is
     using for incoming traffic.
   * `env_template` is a file containing definitions of environment variables.
     These variables are used to configure the container instance without having
     to edit and re-build the Docker image.
   * Replace `<servicename>` with the name you chose in step 4.

6. Test if the service is available

   To test if the now running Docker container is available, open a second SSH
   connection to the training VM. There, run:
   ```
   $ curl localhost:8080/<servicename>/Calculator?wsdl
   ```
   This asks the service for its wsdl (web service description language) file,
   which contains a formal definition of all the methods the service offers.
   You should see an XML file being printed in the console output.

7. Make some test calls to your service
   
   The example service is a simple Calculator service which so far hosts 
   exactly one method: `add` which takes two numbers and returns their sum.
   To test this method, navigate to the `synchronous_service/test_scripts`
   directory. There, a pre-made test script`generic_service_call.py` is
   already available. Edit this file and change lines 14 and 15 again such that
   they contain your full service name. (Compare step 3 of tutorial 1.)

   In the `test_scripts` directory, run:
   ```
   python3 generic_service_call.py add 20 22 local
   ```
   This will call your web service with the numbers 20 and 22 as input and
   print the result. `local` indicates that we are testing the locally
   deployed service.

   You can run `python generic_service_call.py` without further parameters
   to see all available methods (currently that is only one).

   In the SSH window where the Docker container is running, you will see
   log events of the test calls.

8. Stop the local Docker container

   If the above steps worked fine, it is time to deploy the service on the
   CloudFlow platfrom. Therefore, switch to your first SSH window and press
   `Ctrl-C` to stop the running Docker container. Then, proceed to the second
   tutorial.


## Tutorial 2: Deployment on the CloudFlow platform
In your home folder on the training VM, you'll see four linked Python scripts.
These are the deployment scripts. The scripts will enable you to create a new
service on the CloudFlow platform, update it with new revisions of your Docker
image, and obtain status and log information.

Note: Please read each of the following steps completely before executing them.

1. To create a new service, run the following:
   ```
   $ ./service_create_new.py <sericename> <health_check_path>
   ```
   On the CloudFlow platform, this script will create a new Docker repository
   (to which we will later push our Docker image to make it available online)
   and routing rules which will make the service reachable from the outside 
   world under the following address: `https://srv.hetcomp.org/<servicename>/`

   Note: `<servicename>` _must_ be the same as the name chosen in step 2 of 
   tutorial 1. If not, the service will not be reachable (since the service
   will then listen under a different route than the one the load balancer is
   routing.
   
   `<health_check_path>` is what the load balancer will query to check if the
   service is alive. It should be an address that returns a html 200 code, so
   preferrably the wsdl. The complete query path will b
   `https://srv.hetcomp.org/<servicename>/<health_check_path>`. In this
   tutorial, `health_check_path>` should be `"Calculator?wsdl"` (include the
   quotation marks), which is the name of the Java class representing the
   service.

   Important: Run this script exactly once per service! Running this script
   will _not_ make the service available yet, so don't wonder if the URL given
   above doesn't return anything yet.

2. Now, we are ready to actually deploy the service. Therefore, run
   ```
   $ ./service_update.py <servicename> <docker_source_folder> <container_port> <env_filepath>
   ```
   This script will build the docker image, push it to the correct repository,
   and create or update an Amazon task definition and service. The service will
   then automatically spawn container instances on free VM resources.

   * `<servicename>` is the same name as before
   * `<docker_source_folder>`: _absolute_ path to the folder containing the
     Dockerfile
   * `<container_port>`: The port the container listens on. For glassfish,
     that's 8080, and it's the same as the second half of the `-p` port mapping
     in the manual docker run in tutorial 1.
   * ´<env_filepath>` is the path to the file to read environment variables
     from (= `env_template` from the manual run in part 1).

   Deploying the service might take a while, especially for the first time, as
   the Docker image needs to be uploaded to its repository.

   Note: To update an already running service with a new version of the Docker
   image, simply run the exact same command.

3. Once deployed, we can monitor the status of the service by running:
   ```
   $ ./service_get_status.py <servicename>
   ```
   This reports the status of all targets and shows events like container
   spawns etc. Run the script repeatedly to see how the status is changing. 
   It might take a few minutes until the service becomes healthy.

   You can run un `./service_get_status.py ct-sync` to see how it should look
   like for a healthy service. (`ct-sync` is the same calculator service
   already running on the CloudFlow platform.) Look out especially for the
   _health state_ of the service.

5. To get log output from the service itself (such as what requests it processed
   and what the results were), run
   ```
   $ ./service_get_logs.py <servicename>
   ```
   For a healthy service, this report should contain many GET requests from the
   Health Checker which result in the html return code 200. For comparison, you
   can again run `./service_get_logs.py simple-example`.

6. Test the deployed service

   You can now use the same test script as you used in tutorial 1 (step 7), but
   this time use the `deployed` indicator to make calls to your freshly deployed
   service.

   In the `test_scripts` directory, run:
   ```
   python3 generic_service_call.py add 20 22 deployed
   ```
