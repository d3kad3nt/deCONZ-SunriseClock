#!/bin/bash

#Sets up the correct fastlane environment

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

install_package(){
    echo "install $1"
    if command -v apt > /dev/null; then
        sudo apt install $1
    else
        echo "The Package $1 is missing and can't be installed automatically"
        echo "Please install manually and restart the script"
        exit 1
    fi
}

validate_installed_package(){
    package=$1
    if ! command -v $package; then
        install_package $package
    fi
}

if ! command -v ruby > /dev/null; then
    install_package ruby
fi

#TODO find a distribution agnostic way to check for ruby-dev
if ! dpkg -s ruby-dev > /dev/null; then 
    install_package ruby-dev
fi


if ! command -v bundler; then
    echo "Install bundler"
    sudo gem install bundler
fi

bundle config --local path '.ruby_bundle'
bundle install 

