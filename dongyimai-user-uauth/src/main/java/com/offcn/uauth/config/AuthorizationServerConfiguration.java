package com.offcn.uauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer //����OAuth2��Ȩ������
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    //ע�����������
    @Autowired
    private PasswordEncoder passwordEncoder;

    //ע���Զ�����֤����
    @Autowired
    private UserDetailsService userDetailsService;

    //ע����֤������
    @Autowired
    private AuthenticationManager authenticationManager;

    //��Ȩ�������˵����Ȩ����֤��ʽ
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()//���ʷ������˵���Ҫ���пͻ��������֤
                .passwordEncoder(passwordEncoder)//���ÿͻ���������ܻ���
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }

    //�ͻ����˺�����
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       //���ڴ��д����˺�
        clients.inMemory()
                // admin����Ȩ����֤��������֤���ͻ�����֤������֤��ˢ��token
                .withClient("admin") //�˺�����
                .secret(passwordEncoder.encode("admin"))//���룬Ҫ���ü���
                .resourceIds("dongyimai-user", "dongyimai-goods")//��Դ���
                .scopes("server","app")//���÷�Χ
                .authorizedGrantTypes("authorization_code", "password", "refresh_token", "client_credentials","implicit")//��¼��Ȩģʽ
                .redirectUris("http://localhost");//��¼�ɹ���ת��ַ

    }

    //�˵����ƴ洢��ʽ�������Զ�����֤������֤������
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new InMemoryTokenStore()) //���ƴ洢���ڴ�
                  .authenticationManager(authenticationManager)//��֤������
                  .userDetailsService(userDetailsService)//�Զ�����֤��
                  .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);//����˵���ʷ���

    }
}
