package com.example.fypapp

interface CallbackListener {
    fun onDataReceived(data: String, priority: String)
}