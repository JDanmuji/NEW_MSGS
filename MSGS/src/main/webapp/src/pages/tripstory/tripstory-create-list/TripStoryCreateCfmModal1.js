import React, { useState } from "react";
import styles from "./TripStoryCreateCfmModal.module.css";
import { useNavigate } from "react-router-dom";

const TripStoryCreateCfmModal = (props) => {

    const navigate = useNavigate();

    const joinMemberHandler = () => {
        navigate("/tripstory");
    };
    return (
        <div className={styles["modal-background"]}>
            <div className={styles["modal-body"]}>
                <img
                    className={styles["modal-close-btn"]}
                    src={process.env.PUBLIC_URL + "/images/modal_close_btn.png"}
                    alt="closing icon"
                    onClick={props.onClose}
                />
                <div className={styles["confirm-text"]}>
                    <div className={styles["sub-msg"]}>
                        아직 여행을 시작하지 않았습니다😮! <br />
                        대신 다른 사람들의 여행기를 보시겠습니까😊?
                    </div>
                </div>

                <div className={styles["modal-footer"]}>
                    <button
                        onClick={props.onClose}
                        className={`${styles["modal-btn"]} ${styles["cancel-modal-btn"]}`}
                        style={{ color: "black" }}
                    >
                        <span>취소</span>
                    </button>
                    <button
                        onClick={joinMemberHandler}
                        className={`${styles["modal-btn"]} ${styles["confirm-modal-btn"]}`}
                        style={{ color: "white" }}
                    >
                        <span>확인</span>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default TripStoryCreateCfmModal;