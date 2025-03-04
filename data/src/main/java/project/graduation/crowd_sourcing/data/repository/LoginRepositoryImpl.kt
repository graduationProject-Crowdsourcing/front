package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
): LoginRepository {

}