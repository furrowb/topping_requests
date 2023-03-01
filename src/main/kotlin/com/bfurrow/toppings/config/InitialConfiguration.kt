package com.bfurrow.toppings.config

import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class InitialConfiguration(@Autowired private val datasource: DataSource) {

    @Bean
    fun connectionProvider(): DataSourceConnectionProvider =
        DataSourceConnectionProvider(TransactionAwareDataSourceProxy(datasource))

    @Bean
    fun dsl(): DefaultDSLContext = DefaultDSLContext(configuration())

    fun configuration(): DefaultConfiguration {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider())
        jooqConfiguration.set(DefaultExecuteListenerProvider(JooqExceptionTranslator()))
        return jooqConfiguration
    }
}