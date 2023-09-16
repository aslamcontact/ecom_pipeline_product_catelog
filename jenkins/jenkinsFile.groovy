pipeline
        {
            agent {
                docker {
                    image 'ubuntu:latest'
                    args ' -v /var/run/docker.sock:/var/run/docker.sock ' +
                            ' -v /usr/bin/docker:/usr/bin/docker '
                }
            }
            environment{
                volume='artifact'
                gitImage='aslamimages/alpine-git:2'
                buildImage="aslamimages/mvn_jdk_git:latest"
                gitProjectUrl="https://github.com/aslamcontact/ecom_product_catelog.git"
                deployImage="aslamimages/basic_api"

            }



            options {
                skipDefaultCheckout()
            }
            stages {

                stage('cloning')
                        {
                            steps {
                                sh "docker volume rm ${volume}"
                                sh  "docker volume create ${volume}"

                                sh  "docker run --rm  --name test2 "+
                                        "-v ${volume}:/app "+
                                        "-w /app  ${gitImage} "+
                                        "git clone ${gitProjectUrl}"
                                sh  "docker run --rm  --name test3 "+
                                        "-v ${volume}:/app "+
                                        "-v /var/run/docker.sock:/var/run/docker.sock"+
                                        "-v /usr/bin/docker:/usr/bin/docker"+
                                        "-v /usr/bin/docker:/usr/bin/compose"+
                                        "-w /app/ecom_product_catelog  ubuntu:latest "+
                                        "docker compose up -d"

                                sh  "docker run --rm  --name test4 "+
                                        "-v ${volume}:/app "+
                                        "-v /var/run/docker.sock:/var/run/docker.sock"+
                                        "-v /usr/bin/docker:/usr/bin/docker"+
                                        "-v /usr/bin/docker:/usr/bin/compose"+
                                        "-w /app/ecom_product_catelog  ubuntu:latest "+
                                        "docker compose down"



                            }
                        }
            }
        }