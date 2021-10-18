package com.krishlab.utility;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krishlab.entity.UserEntity;
import com.krishlab.service.UserService;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		UsernamePasswordAuthenticationToken uPToken = null;
		UserEntity user;
		
		//validate using Ldap
		HashMap<String, String> ldabUserDetail = validateUsingLdap(username, password);

		if (ldabUserDetail.isEmpty()) {
			user = userService.findUserByNameOrEmail(username,username);
			if (user != null) {
				if (BCrypt.checkpw(password, user.getPassword())) {
					uPToken = new UsernamePasswordAuthenticationToken(
							new User(username, password, userService.getAuthority(user)), password,
							userService.getAuthority(user));
				}
			} else {
				throw new UsernameNotFoundException("User not found with username or email " + username);
			}
		} else {
			//user = userService.findUserByNameOrEmail(ldabUserDetail.get("samaccountname"),ldabUserDetail.get("mail"));
			user = userService.findByUsernameOrEmailOrEmpId(ldabUserDetail.get("samaccountname"),ldabUserDetail.get("mail"),ldabUserDetail.get("employeeid"));
			if (user != null) {
				uPToken = new UsernamePasswordAuthenticationToken(
						new User(username, password, userService.getAuthority(user)), password,
						userService.getAuthority(user));
			} else {
				throw new UsernameNotFoundException("User not found with username or email " + username);
			}
		}

		return uPToken;
		
	}
	
	
	private HashMap<String, String> validateUsingLdap(String username, String password) {
		
		boolean authenciated = false;
		HashMap<String, String> users = new HashMap<String, String>();
		String dn = username + "@tmindia.tatamotors.com";
		String ldapURL = "ldap://tmsndinf01.tmindia.tatamotors.com";

		try
		{
			// Setup environment for authenticating
			Hashtable<String, String> environment = new Hashtable<String, String>();

			environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			environment.put(Context.PROVIDER_URL, ldapURL);
			environment.put(Context.SECURITY_AUTHENTICATION, "simple");
			environment.put(Context.SECURITY_PRINCIPAL, dn);
			environment.put(Context.SECURITY_CREDENTIALS, password);
			
			DirContext authContext = new InitialDirContext(environment);

			authenciated = true; // user is authenticated

			String[] attrIDs = { "samaccountname", "cn", "employeeid", "sn", "givenname", "mail", "manager" };
			String filter = "(sAMAccountName=" + username + ")";

			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(attrIDs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> e = authContext.search("dc=tmindia,dc=tatamotors,dc=com", filter, ctls);
			SearchResult rslt = (SearchResult) e.next();
			Attributes attrs = rslt.getAttributes();

			users.put("authenticated", "true");
			String[] arrSplit = attrs.get("samaccountname").toString().split(":");
			users.put("samaccountname", arrSplit[1].trim());
			arrSplit = attrs.get("cn").toString().split(":");
			users.put("cn", arrSplit[1].trim());
			arrSplit = attrs.get("employeeid").toString().split(":");
			users.put("employeeid", arrSplit[1].trim());
			arrSplit = attrs.get("givenname").toString().split(":");
			users.put("givenname", arrSplit[1].trim());
			arrSplit = attrs.get("mail").toString().split(":");
			users.put("mail", arrSplit[1].trim());
			users.put("manager", attrs.get("manager").toString());
			
			authContext.close();

		}
		catch (Exception ex) {
			authenciated = false;
		}

		finally {
			if (authenciated) {
				authenciated = true;
			} else {
				authenciated = false;
			}
		}
		return users;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
