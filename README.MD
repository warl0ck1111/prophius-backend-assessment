
# BACKEND API

Brief project description goes here.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This project is an exciting endeavor aimed at creating a dynamic and versatile platform using Spring Boot. Our objective is to design, develop, and deploy an API that not only caters to the basic functionalities of social media but also paves the way for future enhancements and innovations.

Project Overview:
Key Requirements:
To achieve this goal, the project entails the following key requirements:
## Features

- Entity Design: We will meticulously design entities for "User," "Post," and "Comment," each having their own set of properties and relationships. These relationships will include one-to-many and many-to-many associations, ensuring data consistency and efficient retrieval.
- CRUD Operations: Implementing CRUD operations for users, posts, and comments is at the core of our API. Users can create, read, update, and delete their content, fostering a seamless user experience.
- Social Interaction: Our API will facilitate social interaction in various forms. Users will be able to follow and unfollow other users, thereby creating connections within the platform. They can also like and comment on posts, fostering engagement and interactivity.
- Version Control: We will follow best practices in version control by using proper GitHub structure, such as tags and branches. This approach ensures that we can introduce new features while maintaining the ability to revert to previous versions if necessary.
- Testing: Rigorous unit testing will be an integral part of our development process. These tests will guarantee the correctness and reliability of our API endpoints, ensuring a robust and stable system.
- Authentication and Authorization: We understand the paramount importance of security and privacy in a social media platform. As such, we will implement user authentication and authorization using Spring Security and JWT (JSON Web Tokens) to safeguard user data and interactions.
- Database Integration: Our API will seamlessly integrate with an appropriate database system, offering flexibility in choices such as H2, MySQL, or PostgreSQL. The chosen database will serve as the reliable storage backend for our platform.

## Getting Started


### Prerequisites

a postgres database with name 'backend' is needed to lunch the application

## Usage

create a postgres database with name 'backend' and lunch the application
## API Documentation
go to http://localhost:9999/swagger-ui/index.html#/ after lunching the application to lunch the swagger documentation for easy test
apis on this project are accessed through 
## Configuration

configurations can be done depending on the platform lunched either on the application.yml file or the docker compose files.


## Challenges
using JPA derived queries to follow and unfollow user seemed like an overkill since it'll first find userId and then find the user trying to follow/unfollow object before then adding it to the list of followers and then saving as opposed to just saving their respective ID's in the user_followers table using native queries 

