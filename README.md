<p>
    <h1 align="center">Python在线实验平台</h1>
</p>
<p align="center">
	<img src="https://img.shields.io/badge/jdk-1.8-orange.svg"/>
    <img src="https://img.shields.io/badge/spring-2.x-yellow.svg"/>
    <img src="https://img.shields.io/badge/mybatis-3.x-blue.svg"/>
    <img src="https://img.shields.io/badge/license-MIT-brightgreen.svg"/>
</p>


## 一、简单讲讲

PyOnline是一款基于SSMP和Vue开发的Python在线实验平台，系统主要提供公告发布、班级管理、实验编辑、Python代码在线运行、实验评分、实验数据记录、实验结果下载等功能。系统为前后端分离架构，前端基于CodeMirror和Vue开发，使用Element UI辅助设计；后端基于SpringBoot框架，使用MybatisPlus完成持久层设计，调用Python3编译器实现核心功能。

## 二、功能介绍

系统中有三种角色：老师、学生、管理员，前两种角色可以自行注册；

### 1. 作为教师，你可以：

- 创建班级，添加，编辑班级简介

Python线上实验服务。老师可以在平台发布实验项目，并组织为对应的章节，调整实验目录结构。学生加入老师的班级后，即可在线上参与实验，在线编写代码并运行测试，查看结果，提交实验。老师

## 三、运行展示



## 四、如何运行项目

  项目前后端分离，建议用IDEA运行后端，WebStorm运行前端，电脑上需要安装Node.js和MySQL，数据库用Navicat操作。

### 1. 后端项目（py_online)

下载后用idea打开项目，初次加载idea会自动下载依赖，最好先配置镜像源或科学上网，这个过程可能需要1h左右。此时可以在MySQL中创建一个名为two hours的数据库，运行后端文件夹中的two hours.sql文件，即可完成数据库初始化。项目加载完成后，在application.yml中修改项目路径为你当前的项目路径，配置好数据库连接，即可运行后端了。

### 2. 前端项目（py_online_front)

下载后用WebStorm打开项目文件夹，命令`npm install`手动安装node_modules，出现爆红和冲突请后缀`--force`。模块导入完成后，需要配置运行栏，选择npm运行，命令为run，Scripts为serve。

前端项目没有做比例适配，建议浏览器使用125%缩放。

## 五、更新日志

- 20230716-解决前端访问图片被拦截：后端InterceptorConfig的excludePath中，静态资源路径从static目录内开始，即不加/static；另外在StaticConfig的addResourceHandlers中，资源替换路径要确保正确；
- 20230716-更新主分支名称；
