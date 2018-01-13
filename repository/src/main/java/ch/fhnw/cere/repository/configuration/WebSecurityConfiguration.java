package ch.fhnw.cere.repository.configuration;


import ch.fhnw.cere.repository.security.AuthenticationTokenFilter;
import ch.fhnw.cere.repository.security.RestAuthenticationEntryPoint;
import ch.fhnw.cere.repository.services.SecurityService;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
@ComponentScan("ch.fhnw.cere.repository")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService apiUserDetailsService;

    @Autowired
    private SecurityService securityService;

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
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
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
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/feedback_repository/authenticate/**").permitAll()
                .antMatchers("/feedback_repository/ping").permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/?", null, true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedbacksettings/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/comments/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/likes/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/dislikes/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/status/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedback_company/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedback_chat/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/end_users/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/\\w{2}/applications/\\d+/feedbacks/?\\?_=\\d+", "POST", true)).permitAll()
                .antMatchers("/feedback_repository/feedback_repository/authenticate/**").permitAll()
                .antMatchers("/feedback_repository/feedback_repository/ping").permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/?", null, true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedbacksettings/?", null, true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/comments/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/likes/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/dislikes/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/status/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedback_company/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/feedback_chat/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/end_users/?", "POST", true)).permitAll()
                .requestMatchers(new RegexRequestMatcher("/feedback_repository/feedback_repository/\\w{2}/applications/\\d+/feedbacks/?\\?_=\\d+", null, true)).permitAll()

                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}