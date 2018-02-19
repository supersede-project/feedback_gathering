package ch.fhnw.cere.orchestrator.configuration;


import ch.fhnw.cere.orchestrator.security.AuthenticationTokenFilter;
import ch.fhnw.cere.orchestrator.security.RestAuthenticationEntryPoint;
import ch.fhnw.cere.orchestrator.services.SecurityService;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@ComponentScan("ch.fhnw.cere.orchestrator")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService apiUserDetailsService;

    @Autowired
    private SecurityService securityService;

    @Value("${supersede.base_path.feedback}")
    private String basePathFeedback;

    @Value("${supersede.base_path.monitoring}")
    private String basePathMonitoring;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.apiUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public SecurityService securityService() {
        return this.securityService;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "X-Requested-With", "Content-Type", "Access-Control-Allow-Headers", "Accept"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();

        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(basePathFeedback + "/authenticate/**").permitAll()
                .antMatchers(basePathFeedback + "/ping").permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathFeedback + "/\\w{2}/applications/\\d+/?\\?_=\\d+", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathFeedback + "/\\w{2}/applications/\\d+/?", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathFeedback + "/\\w{2}/applications/\\d+/user_identification/\\d+/?", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathFeedback + "/\\w{2}/applications/\\d+/user_identification/\\d+/?\\?_=\\d+", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathFeedback + "/\\w{2}/applications/?", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathMonitoring + "/MonitorTypes/?", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathMonitoring + "/MonitorTypes/\\w+/?/Tools/\\w+/?", "GET", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher(basePathMonitoring + "/MonitorTypes/\\w+/?/Tools/\\w+/ToolConfigurations/\\d+/?", "GET", true)).permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}