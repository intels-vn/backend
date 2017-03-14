package web.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lib.dao.ChannelDAO;
import lib.dao.HttpStatusDAO;
import lib.dao.TokenDAO;
import lib.entity.Channel;
import lib.entity.Token;

@RestController
@Api(value = "Channel", description = "API for Channel")
public class ChannelPresenter {
	@Autowired
	private ChannelDAO channelDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private HttpStatusDAO httpStatusDAO;

	@ApiOperation(value = "Get List of Channel", notes = "Get List of Channel", response = Channel.class)
	@GetMapping(value = "/channels", produces = "application/json")
	public Map<String, Object> getChannelList(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("data", "");
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		List<Channel> channel = this.channelDAO.getChannelList();
		if (channel.isEmpty()) {
			result.put("data", "");
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		this.tokenDAO.updateToken(code);
		result.put("data", channel);
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}
}
