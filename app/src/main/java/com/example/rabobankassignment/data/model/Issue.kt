package com.example.rabobankassignment.data.model

import java.util.Date

data class Issue(
    var headers:Headers= Headers("","", "",""),
    var issues: MutableList<IssueDetail> = mutableListOf()
)
data class Headers(
    var firstName: String,
    var surName: String,
    var issueCount: String,
    var dateOfBirth: String
)
data class IssueDetail(
    var firstName: String,
    var surName: String,
    var issueCount: Int,
    var dateOfBirth: Date,
    var avatarUrl: String
)