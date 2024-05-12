import { useContext, useEffect, useState } from "react";
import { IOrderItem } from "../../../interfaces/IOrderItem";
import axios from "axios";
import { IItem } from "../../../interfaces/IItem";
import { UserContext } from "../../../App";
import { useNavigate } from "react-router-dom";

interface IProps {
    item: IOrderItem,
    total: number,
    setTotal: React.Dispatch<React.SetStateAction<number>>
}

function SummaryRow({item, total, setTotal}: IProps) {

    const { userId } = useContext(UserContext);
    const [itemData, setItemData] = useState<IItem>(item.item);
    const navigate = useNavigate();

    useEffect(() => {
        
        if (item.quantity > itemData.stock) {
            alert("Sorry, we don't have that many " + itemData.name + " in stock!");
            setTimeout(() => navigate('/CartPage'), 1000);
        }

        setTotal(total + (itemData.price * item.quantity));

    },[]);

    return (
        <>
        {itemData && <div>
            {/* <h3>{itemData.name}</h3>
            <p>Quantity: {item.quantity}</p>
            <p>Price: ${itemData.price * item.quantity}</p> */}
            <p>{itemData.name} x {item.quantity}    ${itemData.price * item.quantity}</p>
        </div>}
        </>
    );

}

export default SummaryRow;