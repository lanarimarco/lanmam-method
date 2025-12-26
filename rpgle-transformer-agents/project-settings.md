# Project Settings

## Purpose

This file serves as a central configuration file for the modernization project. It declares variables that are referenced in PROMPT.md files across different agents using placeholder syntax `{VAR}`.

When agents process their PROMPT files, placeholders are replaced with the actual values defined here, ensuring consistency across all agents and making it easy to update project-wide settings in a single location.

## Usage

Variables defined in this file can be referenced in any PROMPT.md or template file using the format:
- `{VARIABLE_NAME}` - will be replaced with the value defined below

## Variables
PACKAGE_NAME=com.lanarimarco.modernization
