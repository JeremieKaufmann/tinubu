import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Form } from "react-bootstrap";
import { getPolicies, createPolicy, updatePolicy } from "./apiService";

const STATUS_OPTIONS = ["ACTIVE", "INACTIVE"];

const App = () => {
  const [policies, setPolicies] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [currentPolicy, setCurrentPolicy] = useState({
    id: null,
    name: "",
    status: "ACTIVE",
    coverageStartDate: "",
    coverageEndDate: ""
  });

  useEffect(() => {
    const fetchPolicies = async () => {
      try {
        const data = await getPolicies();
        setPolicies(data);
      } catch (error) {
        console.error("Erreur lors du chargement des polices:", error);
      }
    };
    fetchPolicies();
  }, []);

  const handleShowAddModal = () => {
    setCurrentPolicy({
      id: null,
      name: "",
      status: "ACTIVE",
      coverageStartDate: "",
      coverageEndDate: ""
    });
    setIsEditing(false);
    setShowModal(true);
  };

  const handleShowEditModal = (policy) => {
    setCurrentPolicy(policy);
    setIsEditing(true);
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCurrentPolicy((prev) => ({ ...prev, [name]: value }));
  };

  const handleSavePolicy = async () => {
    try {
      if (isEditing) {
        const updated = await updatePolicy(currentPolicy.id, currentPolicy);
        setPolicies(policies.map((p) => (p.id === updated.id ? updated : p)));
      } else {
        const created = await createPolicy(currentPolicy);
        setPolicies([...policies, created]);
      }
      handleCloseModal();
    } catch (error) {
      console.error("Erreur lors de l'enregistrement:", error);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Gestion des polices d’assurance</h2>
      <Button variant="primary" onClick={handleShowAddModal} className="mb-3">
        Ajouter une police
      </Button>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Statut</th>
            <th>Début Couverture</th>
            <th>Fin Couverture</th>
            <th>Création</th>
            <th>Dernière mise à jour</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {policies.map((policy) => (
            <tr key={policy.id}>
              <td>{policy.id}</td>
              <td>{policy.name}</td>
              <td>{policy.status}</td>
              <td>{policy.coverageStartDate}</td>
              <td>{policy.coverageEndDate}</td>
              <td>{policy.creationDate}</td>
              <td>{policy.lastUpdateDate || "—"}</td>
              <td>
                <Button variant="warning" size="sm" onClick={() => handleShowEditModal(policy)}>
                  Modifier
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Modal pour ajouter/modifier une police */}
      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>{isEditing ? "Modifier la police" : "Ajouter une nouvelle police"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Nom</Form.Label>
              <Form.Control type="text" name="name" value={currentPolicy.name} onChange={handleChange} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Statut</Form.Label>
              <Form.Select name="status" value={currentPolicy.status} onChange={handleChange}>
                {STATUS_OPTIONS.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Début de couverture</Form.Label>
              <Form.Control type="date" name="coverageStartDate" value={currentPolicy.coverageStartDate} onChange={handleChange} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Fin de couverture</Form.Label>
              <Form.Control type="date" name="coverageEndDate" value={currentPolicy.coverageEndDate} onChange={handleChange} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Annuler
          </Button>
          <Button variant="primary" onClick={handleSavePolicy}>
            Enregistrer
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default App;
