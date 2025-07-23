package org.scoula.chapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.chapi.domain.CHOfficetelModelVO;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CHOfficetelModelDTO {
	private int modelUid;

	@JsonProperty("EXCLUSE_AR")
	private String excluseAr;

	@JsonProperty("GP")
	private String gp;

	@JsonProperty("HOUSE_MANAGE_NO")
	private String houseManageNo;

	@JsonProperty("MODEL_NO")
	private String modelNo;

	@JsonProperty("PBLANC_NO")
	private String pblancNo;

	@JsonProperty("SUBSCRPT_REQST_AMOUNT")
	private int subscrptReqstAmount;

	@JsonProperty("SUPLY_AMOUNT")
	private int suplyAmount;

	@JsonProperty("SUPLY_HSHLDCO")
	private int suplyHshldco;

	@JsonProperty("TP")
	private String tp;

	public static CHOfficetelModelDTO of(CHOfficetelModelVO vo) {
		return CHOfficetelModelDTO.builder()
				.modelUid(vo.getModelUid())
				.excluseAr(vo.getExcluseAr())
				.gp(vo.getGp())
				.houseManageNo(vo.getHouseManageNo())
				.modelNo(vo.getModelNo())
				.pblancNo(vo.getPblancNo())
				.subscrptReqstAmount(vo.getSubscrptReqstAmount())
				.suplyAmount(vo.getSuplyAmount())
				.suplyHshldco(vo.getSuplyHshldco())
				.tp(vo.getTp())
				.build();
	}

	public CHOfficetelModelVO toVO() {
		return CHOfficetelModelVO.builder()
				.modelUid(this.modelUid)
				.excluseAr(this.excluseAr)
				.gp(this.gp)
				.houseManageNo(this.houseManageNo)
				.modelNo(this.modelNo)
				.pblancNo(this.pblancNo)
				.subscrptReqstAmount(this.subscrptReqstAmount)
				.suplyAmount(this.suplyAmount)
				.suplyHshldco(this.suplyHshldco)
				.tp(this.tp)
				.build();
	}
}