package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.servlet.BaseServlet;

/**
 * 鐢ㄦ埛妯″潡WEB灞�
 * @author qdmmy6
 *
 */
public class UserServlet1 extends BaseServlet {
	private UserService userService = new UserService();
	
	/**
	 * ajax鐢ㄦ埛鍚嶆槸鍚︽敞鍐屾牎楠�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 鑾峰彇鐢ㄦ埛鍚�
		 */
		String loginname = req.getParameter("loginname");
		/*
		 * 2. 閫氳繃service寰楀埌鏍￠獙缁撴灉
		 */
		boolean b = userService.ajaxValidateLoginname(loginname);
		/*
		 * 3. 鍙戠粰瀹㈡埛绔�
		 */
		resp.getWriter().print(b);
		return null;
	}
	
	/**
	 * ajax Email鏄惁娉ㄥ唽鏍￠獙
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 鑾峰彇Email
		 */
		String email = req.getParameter("email");
		/*
		 * 2. 閫氳繃service寰楀埌鏍￠獙缁撴灉
		 */
		boolean b = userService.ajaxValidateEmail(email);
		/*
		 * 3. 鍙戠粰瀹㈡埛绔�
		 */
		resp.getWriter().print(b);
		return null;
	}
	
	/**
	 * ajax楠岃瘉鐮佹槸鍚︽纭牎楠�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 鑾峰彇杈撳叆妗嗕腑鐨勯獙璇佺爜
		 */
		String verifyCode = req.getParameter("verifyCode");
		/*
		 * 2. 鑾峰彇鍥剧墖涓婄湡瀹炵殑鏍￠獙鐮�
		 */
		String vcode = (String) req.getSession().getAttribute("vCode");
		/*
		 * 3. 杩涜蹇界暐澶у皬鍐欐瘮杈冿紝寰楀埌缁撴灉
		 */
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		/*
		 * 4. 鍙戦�佺粰瀹㈡埛绔�
		 */
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * 娉ㄥ唽鍔熻兘
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 灏佽琛ㄥ崟鏁版嵁鍒癠ser瀵硅薄
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2. 鏍￠獙涔�, 濡傛灉鏍￠獙澶辫触锛屼繚瀛橀敊璇俊鎭紝杩斿洖鍒皉egist.jsp鏄剧ず
		 */
		Map<String,String> errors = validateRegist(formUser, req.getSession());
		if(errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 3. 浣跨敤service瀹屾垚涓氬姟
		 */
		userService.regist(formUser);
		/*
		 * 4. 淇濆瓨鎴愬姛淇℃伅锛岃浆鍙戝埌msg.jsp鏄剧ず锛�
		 */
		//req.setAttribute("code", "success");
		req.setAttribute("msg", "娉ㄥ唽鍔熻兘锛岃鐧婚檰锛�");
		return "f:/jsps/user/login.jsp";
	}
	
	/*
	 * 娉ㄥ唽鏍￠獙
	 * 瀵硅〃鍗曠殑瀛楁杩涜閫愪釜鏍￠獙锛屽鏋滄湁閿欒锛屼娇鐢ㄥ綋鍓嶅瓧娈靛悕绉颁负key锛岄敊璇俊鎭负value锛屼繚瀛樺埌map涓�
	 * 杩斿洖map
	 */
	private Map<String,String> validateRegist(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 鏍￠獙鐧诲綍鍚�
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "鐢ㄦ埛鍚嶄笉鑳戒负绌猴紒");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "鐢ㄦ埛鍚嶉暱搴﹀繀椤诲湪3~20涔嬮棿锛�");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "鐢ㄦ埛鍚嶅凡琚敞鍐岋紒");
		}
		
		/*
		 * 2. 鏍￠獙鐧诲綍瀵嗙爜
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "瀵嗙爜涓嶈兘涓虹┖锛�");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "瀵嗙爜闀垮害蹇呴』鍦�3~20涔嬮棿锛�");
		}
		
		/*
		 * 3. 纭瀵嗙爜鏍￠獙
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "纭瀵嗙爜涓嶈兘涓虹┖锛�");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "涓ゆ杈撳叆涓嶄竴鑷达紒");
		}
		
		/*
		 * 4. 鏍￠獙email
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email涓嶈兘涓虹┖锛�");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email鏍煎紡閿欒锛�");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email宸茶娉ㄥ唽锛�");
		}
		
		/*
		 * 5. 楠岃瘉鐮佹牎楠�
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "楠岃瘉鐮佷笉鑳戒负绌猴紒");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "楠岃瘉鐮侀敊璇紒");
		}
		
		return errors;
	}
	
	/**
	 * 婵�娲诲姛鑳�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
/*
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//  1. 鑾峰彇鍙傛暟婵�娲荤爜
		  //2. 鐢ㄦ縺娲荤爜璋冪敤service鏂规硶瀹屾垚婵�娲�
		    //> service鏂规硶鏈夊彲鑳芥姏鍑哄紓甯�, 鎶婂紓甯镐俊鎭嬁鏉ワ紝淇濆瓨鍒皉equest涓紝杞彂鍒癿sg.jsp鏄剧ず
		  //3. 淇濆瓨鎴愬姛淇℃伅鍒皉equest锛岃浆鍙戝埌msg.jsp鏄剧ず銆�
		 
		String code = req.getParameter("activationCode");
		try {
			userService.activatioin(code);
			req.setAttribute("code", "success");//閫氱煡msg.jsp鏄剧ず瀵瑰彿
			req.setAttribute("msg", "鎭枩锛屾縺娲绘垚鍔燂紝璇烽┈涓婄櫥褰曪紒");
		} catch (UserException e) {
			// 璇存槑service鎶涘嚭浜嗗紓甯�
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");//閫氱煡msg.jsp鏄剧ずX
		}
		return "f:/jsps/msg.jsp";
	}*/
	/**
	 * 淇敼瀵嗙爜銆�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 灏佽琛ㄥ崟鏁版嵁鍒皍ser涓�
		 * 2. 浠巗ession涓幏鍙杣id
		 * 3. 浣跨敤uid鍜岃〃鍗曚腑鐨刼ldPass鍜宯ewPass鏉ヨ皟鐢╯ervice鏂规硶
		 *   > 濡傛灉鍑虹幇寮傚父锛屼繚瀛樺紓甯镐俊鎭埌request涓紝杞彂鍒皃wd.jsp
		 * 4. 淇濆瓨鎴愬姛淇℃伅鍒皉quest涓�
		 * 5. 杞彂鍒癿sg.jsp
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User)req.getSession().getAttribute("sessionUser");
		// 濡傛灉鐢ㄦ埛娌℃湁鐧诲綍锛岃繑鍥炲埌鐧诲綍椤甸潰锛屾樉绀洪敊璇俊鎭�
		if(user == null) {
			req.setAttribute("msg", "鎮ㄨ繕娌℃湁鐧诲綍锛�");
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			userService.updatePassword(user.getUid(), formUser.getNewpass(), 
					formUser.getLoginpass());
			req.setAttribute("msg", "淇敼瀵嗙爜鎴愬姛");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());//淇濆瓨寮傚父淇℃伅鍒皉equest
			req.setAttribute("user", formUser);//涓轰簡鍥炴樉
			return "f:/jsps/user/pwd.jsp";
		}
	}
	
	/**
	 * 閫�鍑哄姛鑳�
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	
	/**
	 * 鐧诲綍鍔熻兘
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 灏佽琛ㄥ崟鏁版嵁鍒癠ser
		 * 2. 鏍￠獙琛ㄥ崟鏁版嵁
		 * 3. 浣跨敤service鏌ヨ锛屽緱鍒癠ser
		 * 4. 鏌ョ湅鐢ㄦ埛鏄惁瀛樺湪锛屽鏋滀笉瀛樺湪锛�
		 *   * 淇濆瓨閿欒淇℃伅锛氱敤鎴峰悕鎴栧瘑鐮侀敊璇�
		 *   * 淇濆瓨鐢ㄦ埛鏁版嵁锛氫负浜嗗洖鏄�
		 *   * 杞彂鍒發ogin.jsp
		 * 5. 濡傛灉瀛樺湪锛屾煡鐪嬬姸鎬侊紝濡傛灉鐘舵�佷负false锛�
		 *   * 淇濆瓨閿欒淇℃伅锛氭偍娌℃湁婵�娲�
		 *   * 淇濆瓨琛ㄥ崟鏁版嵁锛氫负浜嗗洖鏄�
		 *   * 杞彂鍒發ogin.jsp
		 * 6. 鐧诲綍鎴愬姛锛�
		 * 銆�銆�* 淇濆瓨褰撳墠鏌ヨ鍑虹殑user鍒皊ession涓�
		 *   * 淇濆瓨褰撳墠鐢ㄦ埛鐨勫悕绉板埌cookie涓紝娉ㄦ剰涓枃闇�瑕佺紪鐮佸鐞嗐��
		 */
		/*
		 * 1. 灏佽琛ㄥ崟鏁版嵁鍒皍ser
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2. 鏍￠獙
		 */
		Map<String,String> errors = validateLogin(formUser, req.getSession());
		if(errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		
		/*
		 * 3. 璋冪敤userService#login()鏂规硶
		 */
		User user = userService.login(formUser);
		/*
		 * 4. 寮�濮嬪垽鏂�
		 */
		if(user == null) {
			req.setAttribute("msg", "鐢ㄦ埛鍚嶆垨瀵嗙爜閿欒锛�");
			req.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		} else {
				// 淇濆瓨鐢ㄦ埛鍒皊ession
				req.getSession().setAttribute("sessionUser", user);
				// 鑾峰彇鐢ㄦ埛鍚嶄繚瀛樺埌cookie涓�
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname, "utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);//淇濆瓨10澶�
				resp.addCookie(cookie);
				return "r:/qiye_admin/HTML_model/index.jsp";//閲嶅畾鍚戝埌涓婚〉
			}
		}
	
	/*
	 * 鐧诲綍鏍￠獙鏂规硶锛屽唴瀹圭瓑浣犺嚜宸辨潵瀹屾垚
	 */
	private Map<String,String> validateLogin(User formUser, HttpSession session) {
		
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 鏍￠獙鐧诲綍鍚�
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "鐢ㄦ埛鍚嶄笉鑳戒负绌猴紒");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "鐢ㄦ埛鍚嶉暱搴﹀繀椤诲湪3~20涔嬮棿锛�");
		} 
		
		/*
		 * 2. 鏍￠獙鐧诲綍瀵嗙爜
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "瀵嗙爜涓嶈兘涓虹┖锛�");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "瀵嗙爜闀垮害蹇呴』鍦�3~20涔嬮棿锛�");
		}
		
		/*
		 * 5. 楠岃瘉鐮佹牎楠�
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "楠岃瘉鐮佷笉鑳戒负绌猴紒");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "楠岃瘉鐮侀敊璇紒");
		}
		return errors;
	}
}
