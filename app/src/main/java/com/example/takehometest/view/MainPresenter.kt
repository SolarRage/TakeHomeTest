package com.example.takehometest.view

interface MainPresenter {
    fun bind(view: MainView)
    fun onTextChange(text: String)
    fun onDestroy()
}