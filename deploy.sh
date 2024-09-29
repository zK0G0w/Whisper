#!/bin/zsh
# Author: [Your Name]
#
# This script is used to deploy the Whisper project to the server.
# It builds the Spring Boot application and deploys the JAR file.

project_local_path=~/WorkSpace/Whisper
dev_branch=dev
prod_branch=main
server=aliyun-ecs01
whisper_path=/home/Whisper  # 服务器上的目标路径

function echo_msg() {
  case $1 in
    0)
      echo -e "\033[30;42m $2 \033[0m"  # Success message (green background)
      ;;
    1)
      echo -e "\033[30;41m $2 \033[0m"  # Error message (red background)
      ;;
    *)
      echo "$2"  # Normal message
      ;;
  esac
}

module_name='whisper'
server_path=$whisper_path

function deploy_springboot() {
  git_merge
  echo_msg 0 "Building $module_name"
  mvn clean package -Dmaven.test.skip=true  # 跳过测试打包 JAR

  echo_msg 0 "Transferring JAR to server"
  scp target/${module_name}-0.0.1.jar ${server}:${server_path}/  # 传输 JAR 包到服务器

  # 使用 echo + read 实现类似的提示
  echo "Restart $module_name right now? [y/n]:"
  read restart
#  read -p "Restart $module_name right now? [y/n]: " restart
  case $restart in
    y)
      echo_msg 0 "Restarting application on the server"
      ssh $server "cd ${server_path} && sh run.sh"  # 重启服务
      ;;
    *)
      echo_msg 0 "Skipping restart"
      ;;
  esac
}

function git_merge() {
  cd ${project_local_path}
  branch=$(git rev-parse --abbrev-ref HEAD)

  if [[ $branch = $dev_branch ]]; then
    echo_msg 0 "Currently on $branch, switching to $prod_branch"
    git checkout $prod_branch
    echo_msg 0 "Merging $branch into $prod_branch"
    git merge -m "Merge branch '$branch' into $prod_branch" "$branch"
  elif [[ $branch = $prod_branch ]]; then
    echo_msg 0 "Already on $prod_branch"
  else
    echo_msg 1 "ERROR: Currently on an unsupported branch '$branch'"
    exit 1
  fi
}

function menu() {
  echo_msg 0 "Starting Whisper project deployment"
  deploy_springboot
  echo_msg 0 "Deployment completed!"
}

menu
read -n 1 -p "Press any key to exit..."