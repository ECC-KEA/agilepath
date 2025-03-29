package dev.ecckea.agilepath.backend.shared.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Logged {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)
}