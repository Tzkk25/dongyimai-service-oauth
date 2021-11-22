import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

        /***
         * 校验令牌
         */
        @Test
        public void testParseToken(){
            //令牌
            String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJ1aml1eWUiLCJpZCI6IjEifQ.LJnRwhSw7Mg2k60dFLZooXQRjs4ofuXrCFjmSenIER72rFdHB58cKenSp2aojnuqVUSMe0lhVV1wsfosZzfu8NfUQnMLqdptE40LTp5ukfzWbtfJz8rlLrnDWqOjmRoWeEMzNX7FyA3zQvYKV1LOrykOH6HVGxK7jb-SZdBH4-rWkTg2AyliGdt-uy2FwZTjt1Cu5T4zO1JD28d8VYX5YtNiJ07kcHBUQB4V2Lk5lUeleUvinmZwgTdufhqNnYOSlgJT-EB28rzoclzEyOtD4EbyBzBegY_9zEvvfHYkGX1Tw-KZR0Hg-GSN_WrxIefsTXX4aQen4QR3GHzEkbN4jA";

            //公钥
            String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyO2N5q+RRECgSAluNGEULdAy81X8sV+FFh/HC7JF7jmMWGOb7B2oQsW3v+/4Th2qUYuD6lJZwvWJGU2yTJFJIifVPF2yEJZAqt+HTBSZFWdVo+O/k7MShJXr7Ena+u23YqkukbZwv2uMszMMbpcOXTBb1fjBY7TZy8kez5D2DuPtOO4D9+JVczNe6xJepSfeGT3/SA87VCPHSVKXknZb4t2ffVR5S8RpRsJ8NzYniCelUxsV/qawy/ywxV1x6z0s1Z4qNUlz9KzActOAiS8eHJrFPFlZLgUpuME83fG6hNlkYbHaTvKNhQL/2uh+qquYHUaewCc+T7kXQwEH05vhIQIDAQAB-----END PUBLIC KEY-----";

            //校验Jwt
            Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

            //获取Jwt原始内容
            String claims = jwt.getClaims();
            System.out.println(claims);

            //jwt令牌
            String encoded = jwt.getEncoded();

            System.out.println(encoded);
    }
}
