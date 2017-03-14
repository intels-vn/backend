package web.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lib.dao.HttpStatusDAO;
import lib.dao.MatchDAO;
import lib.dao.TokenDAO;
import lib.entity.Matcheess;
import lib.entity.Token;

@RestController
@Api(value = "Match", description = "API for Match")
public class MatchPresenter {
	@Autowired
	private MatchDAO matchDAO;
	
	@Autowired
	private TokenDAO tokenDAO;
	
	@Autowired
	private HttpStatusDAO httpStatusDAO;
	
	@ApiOperation(value = "Get Match Info", notes = "Get Match Info", response = Matcheess.class)
	@GetMapping(value = "/channels/{id}", produces = "application/json")
	public Map<String, Object> getMatchInfo(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable Long id) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("data", "");
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		
		List<Matcheess> matcheess = this.matchDAO.getMatchInfo(id);
		if(matcheess.isEmpty()){
			result.put("data", "");
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		this.tokenDAO.updateToken(code);
		result.put("data", matcheess);
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}
	
	@ApiOperation(value = "Bet request", notes = "Send bet request", response = Matcheess.class)
	@PostMapping(value = "/bet", produces = "application/json")
	public Map<String, Object> postBetRequest(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @RequestParam Long matchId, @RequestParam String userId) {
		return null;
	}
}
