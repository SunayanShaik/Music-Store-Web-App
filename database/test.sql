select * from invoice i, lineitem l, product p 
where i.invoice_id = l.invoice_id
and l.product_id = p.product_id;