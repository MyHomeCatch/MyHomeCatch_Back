package org.scoula.chapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.domain.CHOfficetelCmpetVO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelCmpetDTO {
	private int cmpetUid;

	@JsonProperty("CMPET_RATE")
	private String cmpetRate;
	@JsonProperty("HOUSE_MANAGE_NO")
	private String houseManageNo;
	@JsonProperty("HOUSE_TY")
	private String houseTy;
	@JsonProperty("MODEL_NO")
	private String modelNo;
	@JsonProperty("PBLANC_NO")
	private String pblancNo;
	@JsonProperty("REQ_CNT")
	private int reqCnt;
	@JsonProperty("RESIDNT_PRIOR_AT")
	private String residntPriorAt;
	@JsonProperty("RESIDNT_PRIOR_SENM")
	private String residntPriorSenm;
	@JsonProperty("SUPLY_HSHLDCO")
	private int suplyHshldco;

	public static CHOfficetelCmpetDTO of(CHOfficetelCmpetVO vo) {
		return CHOfficetelCmpetDTO.builder()
				.cmpetUid(vo.getCmpetUid())
				.cmpetRate(vo.getCmpetRate())
				.houseManageNo(vo.getHouseManageNo())
				.houseTy(vo.getHouseTy())
				.modelNo(vo.getModelNo())
				.pblancNo(vo.getPblancNo())
				.reqCnt(vo.getReqCnt())
				.residntPriorAt(vo.getResidntPriorAt())
				.residntPriorSenm(vo.getResidntPriorSenm())
				.suplyHshldco(vo.getSuplyHshldco())
				.build();
	}

	public CHOfficetelCmpetVO toVO() {
		return CHOfficetelCmpetVO.builder()
				.cmpetUid(this.cmpetUid)
				.cmpetRate(this.cmpetRate)
				.houseManageNo(this.houseManageNo)
				.houseTy(this.houseTy)
				.modelNo(this.modelNo)
				.pblancNo(this.pblancNo)
				.reqCnt(this.reqCnt)
				.residntPriorAt(this.residntPriorAt)
				.residntPriorSenm(this.residntPriorSenm)
				.suplyHshldco(this.suplyHshldco)
				.build();
	}
}