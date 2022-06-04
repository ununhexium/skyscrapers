package net.lab0.skyscrapers.api.http4k

class InvalidAuthentication(val value: String) :
  Exception("Can't use the Authorization header's value of '$value' to authorise the player.")
