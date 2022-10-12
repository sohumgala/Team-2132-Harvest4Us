package com.amm.harvest4us

import com.amm.harvest4us.backend.FlaskBackendConnect
import com.amm.harvest4us.backend.MockBackendConnect

object MockBackend : MockBackendConnect() // Use to mock all backend calls
object FlaskBackend : FlaskBackendConnect() // Use with Flask backend
