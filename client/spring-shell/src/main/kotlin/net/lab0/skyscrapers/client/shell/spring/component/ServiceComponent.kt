package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import org.springframework.stereotype.Component

@Component
class ServiceComponent: Service by ServiceImpl.new()
